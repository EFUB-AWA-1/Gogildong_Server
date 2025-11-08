package com.efub.gogildong.rank.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.rank.dto.GlobalRankItemDto;
import com.efub.gogildong.rank.dto.SchoolRankItemDto;
import com.efub.gogildong.rank.dto.response.*;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankService {

    private static final String GLOBAL_KEY = "rank:global";
    private static final String SCHOOL_KEY_FMT = "rank:school:%d";
    private static final String SCHOOLS_KEY = "rank:schools";
    private static final int TOP_LIMIT = 100; // 상위 100명 반환, 나중에 환경변수로 넣는 게 좋을듯

    private final StringRedisTemplate redis;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    // Redis Rebuild
    @Transactional
    public void rebuildAllRanksFromDB() {
        try {
            // 0) 기존 키 정리
            redis.delete(GLOBAL_KEY);
            redis.delete(SCHOOLS_KEY);
            var schoolKeys = redis.keys("rank:school:*");
            if (!schoolKeys.isEmpty()) redis.delete(schoolKeys);

            // 1) 전체 유저를 돌며 글로벌/교내 채우고, 학교 합계를 누적
            var z = redis.opsForZSet();
            Map<Long, Long> schoolSum = new HashMap<>();

            userRepository.findAll().forEach(u -> {
                long uid = u.getUserId();
                long score = u.getTotal_score();
                z.add(GLOBAL_KEY, memberUser(uid), score);

                var school = u.getSchool();
                if (school != null) {
                    long sid = school.getSchoolId();
                    z.add(schoolKey(sid), memberUser(uid), score);
                    schoolSum.merge(sid, score, Long::sum);
                }
            });

            schoolSum.forEach((sid, sum) -> z.add(SCHOOLS_KEY, memberSchool(sid), sum));
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // 사용자 점수 upsert
    @Transactional
    public void upsertUserScore(long userId, long newScore) {
        try {
            var z = redis.opsForZSet();

            Double prev = z.score(GLOBAL_KEY, memberUser(userId));
            double oldScore = prev == null ? 0d : prev;

            z.add(GLOBAL_KEY, memberUser(userId), newScore);

            userRepository.findById(userId).ifPresentOrElse(u -> {
                if (u.getSchool() != null) {
                    long sid = u.getSchool().getSchoolId();
                    z.add(schoolKey(sid), memberUser(userId), newScore);
                    double delta = newScore - oldScore;
                    if (delta != 0) {
                        z.incrementScore(SCHOOLS_KEY, memberSchool(sid), delta);
                    }
                }
            }, () -> {
                throw new GoGildongException(ExceptionCode.USER_NOT_FOUND);
            });
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // 사용자 학교 변경 시 호출: 이전/새 학교 키에서 이동 처리
    @Transactional
    public void onUserSchoolChanged(long userId, Long oldSchoolId, Long newSchoolId) {
        try {
            var z = redis.opsForZSet();
            Double sObj = z.score(GLOBAL_KEY, memberUser(userId));
            double s = sObj == null ? 0d : sObj;

            if (oldSchoolId != null) {
                z.remove(schoolKey(oldSchoolId), memberUser(userId));
                z.incrementScore(SCHOOLS_KEY, memberSchool(oldSchoolId), -s);
            }
            if (newSchoolId != null) {
                z.add(schoolKey(newSchoolId), memberUser(userId), s);
                z.incrementScore(SCHOOLS_KEY, memberSchool(newSchoolId), s);
            }
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank : 전체 랭킹 조회
    public GlobalRankListResponse getGlobalTop() {
        try {
            ZSetOperations<String, String> z = redis.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> rows =
                    z.reverseRangeWithScores(GLOBAL_KEY, 0, TOP_LIMIT - 1);

            if (rows == null || rows.isEmpty()) {
                throw new GoGildongException(ExceptionCode.RANK_NOT_FOUND);
            }

            List<Long> userIds = rows.stream()
                    .map(t -> parseUserId(t.getValue()))
                    .toList();

            Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));

            List<GlobalRankItemDto> items = new ArrayList<>(rows.size());
            int i = 0;
            for (ZSetOperations.TypedTuple<String> t : rows) {
                long uid = parseUserId(t.getValue());
                long score = t.getScore() == null ? 0L : t.getScore().longValue();
                User u = userMap.get(uid);

                items.add(GlobalRankItemDto.builder()
                        .rank(++i)
                        .user_id(uid)
                        .user_name(u != null ? u.getUsername() : "(알 수 없음)")
                        .total_score(score)
                        .build());
            }

            return GlobalRankListResponse.builder().rankings(items).build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank/mine : 전체 내 랭킹 조회
    public MyGlobalRankResponse getMyGlobalRank(long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        try {
            ZSetOperations<String, String> z = redis.opsForZSet();

            Long zeroBasedRank = z.reverseRank(GLOBAL_KEY, memberUser(userId));
            Double score = z.score(GLOBAL_KEY, memberUser(userId));
            Long total = z.zCard(GLOBAL_KEY);

            Integer rank1Based = zeroBasedRank == null ? null : (int) (zeroBasedRank + 1);
            long myScore = score == null ? 0L : score.longValue();

            Double percentile = bottomPercentile(rank1Based, total);

            return MyGlobalRankResponse.builder()
                    .user_id(userId)
                    .user_name(u.getUsername())
                    .score(myScore)
                    .global_rank(rank1Based)
                    .global_percentile(percentile)
                    .build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank/school : 교내 전체 랭킹 조회
    public GlobalRankListResponse getSchoolTopByUser(long userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
        if (me.getSchool() == null) {
            throw new GoGildongException(ExceptionCode.USER_HAS_NO_SCHOOL);
        }

        try {
            long schoolId = me.getSchool().getSchoolId();
            String key = schoolKey(schoolId);

            ZSetOperations<String, String> z = redis.opsForZSet();
            Set<ZSetOperations.TypedTuple<String>> rows =
                    z.reverseRangeWithScores(key, 0, TOP_LIMIT - 1);

            if (rows == null || rows.isEmpty()) {
                throw new GoGildongException(ExceptionCode.RANK_NOT_FOUND);
            }

            List<Long> userIds = rows.stream()
                    .map(t -> parseUserId(t.getValue()))
                    .toList();

            Map<Long, User> userMap = userRepository.findAllById(userIds).stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));

            List<GlobalRankItemDto> items = new ArrayList<>(rows.size());
            int i = 0;
            for (ZSetOperations.TypedTuple<String> t : rows) {
                long uid = parseUserId(t.getValue());
                long score = t.getScore() == null ? 0L : t.getScore().longValue();
                User u = userMap.get(uid);

                items.add(GlobalRankItemDto.builder()
                        .rank(++i)
                        .user_id(uid)
                        .user_name(u != null ? u.getUsername() : "(알 수 없음)")
                        .total_score(score)
                        .build());
            }
            return GlobalRankListResponse.builder().rankings(items).build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank/school/mine : 교내 내 랭킹 조회
    public SchoolMyRankResponse getMySchoolRank(long userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
        if (me.getSchool() == null) {
            throw new GoGildongException(ExceptionCode.USER_HAS_NO_SCHOOL);
        }

        try {
            long schoolId = me.getSchool().getSchoolId();
            String key = schoolKey(schoolId);

            ZSetOperations<String, String> z = redis.opsForZSet();
            Long zeroBasedRank = z.reverseRank(key, memberUser(userId));
            Double score = z.score(key, memberUser(userId));
            Long total = z.zCard(key);

            Integer rank1Based = zeroBasedRank == null ? null : (int) (zeroBasedRank + 1);
            long myScore = score == null ? 0L : score.longValue();

            Double percentile = bottomPercentile(rank1Based, total);

            return SchoolMyRankResponse.builder()
                    .user_id(me.getUserId())
                    .user_name(me.getUsername())
                    .score(myScore)
                    .my_rank(rank1Based)
                    .my_percentile(percentile)
                    .build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank/schools : 전체 학교별 랭킹 조회
    public SchoolRankListResponse getSchoolsBoard() {
        try {
            var z = redis.opsForZSet();
            var rows = z.reverseRangeWithScores(SCHOOLS_KEY, 0, TOP_LIMIT - 1);
            if (rows == null || rows.isEmpty()) {
                throw new GoGildongException(ExceptionCode.RANK_NOT_FOUND);
            }

            List<Long> schoolIds = rows.stream()
                    .map(t -> Long.parseLong(Objects.requireNonNull(t.getValue()).substring(2)))
                    .toList();

            Map<Long, School> byId = schoolRepository.findAllById(schoolIds).stream()
                    .collect(Collectors.toMap(School::getSchoolId, s -> s));

            List<SchoolRankItemDto> items = new ArrayList<>();
            int i = 0;
            for (var t : rows) {
                long sid = Long.parseLong(Objects.requireNonNull(t.getValue()).substring(2));
                long sum = t.getScore() == null ? 0L : t.getScore().longValue();
                School s = byId.get(sid);

                items.add(SchoolRankItemDto.builder()
                        .rank(++i)
                        .school_id(sid)
                        .school_name(s != null ? s.getSchoolName() : "(알 수 없음)")
                        .total_score(sum)
                        .build());
            }
            return SchoolRankListResponse.builder().rankings(items).build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // GET /rank/schools/mine : 전체 학교 중 내 학교 랭킹 조회
    public MySchoolVsSchoolResponse getMySchoolVsSchoolRank(long userId) {
        User me = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
        if (me.getSchool() == null) {
            throw new GoGildongException(ExceptionCode.USER_HAS_NO_SCHOOL);
        }

        try {
            long sid = me.getSchool().getSchoolId();
            var z = redis.opsForZSet();

            Long zeroBased = z.reverseRank(SCHOOLS_KEY, memberSchool(sid));
            Double score = z.score(SCHOOLS_KEY, memberSchool(sid));
            Long total = z.zCard(SCHOOLS_KEY);

            Integer rank1 = zeroBased == null ? null : (int)(zeroBased + 1);
            long schoolTotal = score == null ? 0L : score.longValue();

            Double pct = bottomPercentile(rank1, total);

            return MySchoolVsSchoolResponse.builder()
                    .user_id(userId)
                    .school_id(sid)
                    .school_name(me.getSchool().getSchoolName())
                    .score(schoolTotal)              // 우리 학교 총합 점수
                    .school_rank(rank1)
                    .school_percentile(pct)
                    .build();
        } catch (DataAccessException e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    // Util method
    private static String memberUser(long userId) {
        return "u:" + userId;
    }
    private static long parseUserId(String m) {
        return Long.parseLong(m.substring(2));
    }
    private static String memberSchool(long schoolId) {
        return "s:" + schoolId;
    }
    private static String schoolKey(long schoolId) {
        return String.format(SCHOOL_KEY_FMT, schoolId);
    }
    private static Double bottomPercentile(Integer rank1Based, Long total) {
        if (rank1Based == null || total == null || total <= 0) return null;
        double p = ((rank1Based - 1) / (double) total) * 100.0;
        return Math.round(p * 10.0) / 10.0;
    }
}
