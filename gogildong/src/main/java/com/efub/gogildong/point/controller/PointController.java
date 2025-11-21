package com.efub.gogildong.point.controller;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.point.service.PointService;
import com.efub.gogildong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final UserService userService;

    /* GET /point : 사용자 포인트 조회 */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> getMyPoints(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        int total = pointService.getTotalPoints(userId);
        return Map.of("user_id", userId, "totalPoints", total);
    }

    /* POST /point/add : 포인트 수동 추가 */
    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> addPoints(
            @RequestParam int amount,
            Authentication authentication
    ) {
        if (amount <= 0) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        String loginId = authentication.getName();
        long userId = userService.getUserByLoginId(loginId).getUserId();

        int updated = pointService.addPoints(userId, amount);
        return Map.of("user_id", userId, "added", amount, "totalPoints", updated);
    }

    /* POST /point/deduct : 포인트 수동 차감 */
    @PostMapping("/deduct")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> deductPoints(
            @RequestParam int amount,
            Authentication authentication
    ) {
        if (amount <= 0) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        String loginId = authentication.getName();
        long userId = userService.getUserByLoginId(loginId).getUserId();

        int updated = pointService.deductPoints(userId, amount);
        return Map.of("user_id", userId, "deducted", amount, "totalPoints", updated);
    }
}
