package com.efub.gogildong.rank.controller;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.rank.dto.response.*;
import com.efub.gogildong.rank.service.RankService;
import com.efub.gogildong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;
    private final UserService userService;

    /* Redis Test용 API, Super Admin 권한 줘야 사용할 수 있게 변경하는 게 좋을 듯 */
    @PostMapping("/rebuild")
    public void rebuild() {
        try {
            rankService.rebuildAllRanksFromDB();
        } catch (Exception e) {
            throw new GoGildongException(ExceptionCode.REDIS_OPERATION_FAILED);
        }
    }

    /* GET /rank : 전체 랭킹 조회 (상위 100) */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public GlobalRankListResponse getGlobal(Authentication authentication) {
        return rankService.getGlobalTop();
    }

    /* GET /rank/mine : 전체에서 내 랭킹 조회 */
    @GetMapping("/mine")
    @PreAuthorize("isAuthenticated()")
    public MyGlobalRankResponse getMyGlobal(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        return rankService.getMyGlobalRank(userId);
    }

    /* GET /rank/school : 교내 전체 랭킹 조회 */
    @GetMapping("/school")
    @PreAuthorize("isAuthenticated()")
    public GlobalRankListResponse getSchool(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        return rankService.getSchoolTopByUser(userId);
    }

    /* GET /rank/school/mine : 교내 내 랭킹 조회 */
    @GetMapping("/school/mine")
    @PreAuthorize("isAuthenticated()")
    public SchoolMyRankResponse getMySchool(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        return rankService.getMySchoolRank(userId);
    }

    /* GET /rank/schools : 전체 학교별 랭킹 */
    @GetMapping("/schools")
    @PreAuthorize("isAuthenticated()")
    public SchoolRankListResponse getSchools() {
        return rankService.getSchoolsBoard();
    }

    /* GET /rank/schools/mine : 내 학교의 학교랭킹 */
    @GetMapping("/schools/mine")
    @PreAuthorize("isAuthenticated()")
    public MySchoolVsSchoolResponse getMySchoolVsSchool(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        return rankService.getMySchoolVsSchoolRank(userId);
    }
}
