package com.efub.gogildong.quiz.controller;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.quiz.dto.request.SubmitQuizRequest;
import com.efub.gogildong.quiz.dto.response.QuizDetailResponse;
import com.efub.gogildong.quiz.dto.response.QuizListResponse;
import com.efub.gogildong.quiz.service.QuizCommandService;
import com.efub.gogildong.quiz.service.QuizQueryService;
import com.efub.gogildong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizQueryService quizQueryService;
    private final QuizCommandService quizCommandService;
    private final UserService userService;

    /* GET /quiz : 전체 퀴즈 목록 */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public QuizListResponse getAll(Authentication authentication) {
        String loginId = authentication.getName();
        if (loginId == null || loginId.isBlank()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        long userId = userService.getUserByLoginId(loginId).getUserId();
        return quizQueryService.getAllQuizzes(userId);
    }

    /* GET /quiz/{quizId} : 퀴즈 상세 */
    @GetMapping("/{quizId}")
    @PreAuthorize("isAuthenticated()")
    public QuizDetailResponse getOne(
            @PathVariable Long quizId,
            Authentication authentication
    ) {
        if (quizId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        String loginId = authentication.getName();
        long userId = userService.getUserByLoginId(loginId).getUserId();
        return quizQueryService.getQuizDetail(quizId, userId);
    }

    /* POST /quiz/{quizId} 퀴즈 제출 */
    @PostMapping("/{quizId}")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> submit(
            @PathVariable Long quizId,
            @RequestBody SubmitQuizRequest request,
            Authentication authentication
    ) {
        if (quizId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        String loginId = authentication.getName();
        Long userId = userService.getUserByLoginId(loginId).getUserId();
        return quizCommandService.submit(quizId, userId, request);
    }

}
