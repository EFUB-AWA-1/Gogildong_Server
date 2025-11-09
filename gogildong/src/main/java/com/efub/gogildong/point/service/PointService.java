package com.efub.gogildong.point.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.rank.service.RankService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;
    private final RankService rankService;

    // Point 추가
    @Transactional
    public int addPoints(Long userId, int amount) {
        if (userId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        int before = user.getTotal_score();
        int after = before + amount;

        user.setTotal_score(after);
        rankService.upsertUserScore(userId, after);

        return after;
    }

    // Point 차감
    @Transactional
    public int deductPoints(Long userId, int amount) {
        if (userId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        int before = user.getTotal_score();
        int after = Math.max(0, before - amount);

        user.setTotal_score(after);
        rankService.upsertUserScore(userId, after);

        return after;
    }

    // 사용자 포인트 조회
    @Transactional(readOnly = true)
    public int getTotalPoints(Long userId) {
        if (userId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
        return user.getTotal_score();
    }
}
