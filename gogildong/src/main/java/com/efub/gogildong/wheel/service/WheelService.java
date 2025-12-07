package com.efub.gogildong.wheel.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import com.efub.gogildong.wheel.domain.MemberMainWheelchair;
import com.efub.gogildong.wheel.domain.Wheelchair;
import com.efub.gogildong.wheel.dto.WheelList;
import com.efub.gogildong.wheel.dto.request.WheelchairCreateRequest;
import com.efub.gogildong.wheel.dto.response.WheelchairResponse;
import com.efub.gogildong.wheel.repository.MemberMainWheelchairRepository;
import com.efub.gogildong.wheel.repository.WheelchairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WheelService {

    private static final String WHEELCHAIR_BOOKMARK_RANK_KEY = "wheelchair:bookmarkRank";

    private final StringRedisTemplate stringRedisTemplate;
    private final WheelchairRepository wheelchairRepository;
    private final MemberMainWheelchairRepository mmwRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleBookmark(Long userId, Long wheelchairId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        Wheelchair wheelchair = wheelchairRepository.findById(wheelchairId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.WHEEL_NOT_FOUND));

        Optional<MemberMainWheelchair> existing =
                mmwRepository.findByUserAndWheelchair(user, wheelchair);

        String memberKey = wheelchairId.toString();

        if (existing.isPresent()) {
            // 이미 즐겨찾기 → 해제
            mmwRepository.delete(existing.get());
            wheelchair.decreaseBookmarkCount();

            stringRedisTemplate.opsForZSet()
                    .incrementScore(WHEELCHAIR_BOOKMARK_RANK_KEY, memberKey, -1);

            return false;
        } else {
            // 아직 즐겨찾기 안 됨 → 새로 추가
            mmwRepository.save(new MemberMainWheelchair(user, wheelchair));
            wheelchair.increaseBookmarkCount();

            stringRedisTemplate.opsForZSet()
                    .incrementScore(WHEELCHAIR_BOOKMARK_RANK_KEY, memberKey, 1);

            return true;
        }
    }

    public WheelList getWheelList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        // 1) 이 유저가 즐겨찾기한 휠체어 id 목록
        List<MemberMainWheelchair> userBookmarks = mmwRepository.findByUser(user);
        Set<Long> userBookmarkIds = userBookmarks.stream()
                .map(m -> m.getWheelchair().getId())
                .collect(Collectors.toSet());

        // 2) Redis ZSET에서 전역 TOP 5 휠체어 ID 가져오기
        Set<String> topIdsStr = stringRedisTemplate.opsForZSet()
                .reverseRange(WHEELCHAIR_BOOKMARK_RANK_KEY, 0, 4);

        List<Long> topIds = new ArrayList<>();

        if (topIdsStr != null && !topIdsStr.isEmpty()) {
            topIds = topIdsStr.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }

        // Redis에 아직 아무 데이터 없을 경우
        if (topIds.isEmpty()) {
            List<Wheelchair> fallback = wheelchairRepository
                    .findTop5ByOrderByBookmarkCountDesc();
            List<WheelchairResponse> fallbackRes = fallback.stream()
                    .map(w -> WheelchairResponse.of(w, userBookmarkIds.contains(w.getId())))
                    .collect(Collectors.toList());
            return new WheelList(fallbackRes);
        }

        // 3) 실제 휠체어 엔티티 조회
        List<Wheelchair> wheelchairs = wheelchairRepository.findAllById(topIds);
        Map<Long, Wheelchair> wheelMap = wheelchairs.stream()
                .collect(Collectors.toMap(Wheelchair::getId, w -> w));

        List<WheelchairResponse> responses = topIds.stream()
                .map(wheelMap::get)
                .filter(Objects::nonNull)
                .map(w -> WheelchairResponse.of(
                        w,
                        userBookmarkIds.contains(w.getId())  // mainUsed 여부
                ))
                .collect(Collectors.toList());

        return new WheelList(responses);
    }

    public WheelList searchWheelchairs(Long userId, String keyword, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        // 이 유저가 즐겨찾기한 휠체어 id 리스트
        List<MemberMainWheelchair> userBookmarks = mmwRepository.findByUser(user);
        Set<Long> userBookmarkIds = userBookmarks.stream()
                .map(m -> m.getWheelchair().getId())
                .collect(Collectors.toSet());

        PageRequest pageable = PageRequest.of(page, size);

        var pageResult = wheelchairRepository
                .findByWheelchairNameContainingIgnoreCase(keyword, pageable);

        List<WheelchairResponse> responses = pageResult.getContent().stream()
                .map(w -> WheelchairResponse.of(
                        w,
                        userBookmarkIds.contains(w.getId())
                ))
                .collect(Collectors.toList());

        return new WheelList(responses);
    }

    @Transactional
    public void deleteBookmark(Long userId, Long wheelchairId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        Wheelchair wheelchair = wheelchairRepository.findById(wheelchairId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.WHEEL_NOT_FOUND));

        MemberMainWheelchair relation = mmwRepository
                .findByUserAndWheelchair(user, wheelchair)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.WHEELMARK_NOT_FOUND));

        // DB 관계 삭제
        mmwRepository.delete(relation);
        wheelchair.decreaseBookmarkCount();

        // Redis 랭킹 감소
        stringRedisTemplate.opsForZSet()
                .incrementScore(WHEELCHAIR_BOOKMARK_RANK_KEY, wheelchairId.toString(), -1);
    }

    @Transactional
    public WheelchairResponse createWheelchair(WheelchairCreateRequest request) {
        Wheelchair wheelchair = new Wheelchair(
                request.getWheelChairName(),
                request.getWidth(),
                request.getMaxThreshold()
        );

        Wheelchair saved = wheelchairRepository.save(wheelchair);

        return WheelchairResponse.of(saved, false);
    }
}
