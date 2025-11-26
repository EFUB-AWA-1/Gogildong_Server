package com.efub.gogildong.quiz.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.point.service.PointService;
import com.efub.gogildong.quiz.domain.Quiz;
import com.efub.gogildong.quiz.domain.QuizAttempt;
import com.efub.gogildong.quiz.domain.QuizChoice;
import com.efub.gogildong.quiz.dto.request.SubmitQuizRequest;
import com.efub.gogildong.quiz.repository.QuizAttemptRepository;
import com.efub.gogildong.quiz.repository.QuizChoiceRepository;
import com.efub.gogildong.quiz.repository.QuizRepository;
import com.efub.gogildong.shops.service.CoinService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizCommandService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final QuizChoiceRepository quizChoiceRepository;
    private final PointService pointService;

    private static final int CORRECT_POINT = 3;
    private final CoinService coinService;

    @Transactional
    public Map<String, Object> submit(Long quizId, Long userId, SubmitQuizRequest req) {
        if (quizId == null || userId == null) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }
        if (req == null || req.selectedLabel() == null || req.selectedLabel().isBlank()) {
            throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT));

        String selected = req.selectedLabel().trim().toUpperCase();
        QuizChoice chosen = quizChoiceRepository.findByQuiz_IdAndLabel(quizId, selected)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT));

        boolean isCorrect = quiz.getCorrectChoice().getLabel().equalsIgnoreCase(selected);

        QuizAttempt attempt = quizAttemptRepository.findByUser_UserIdAndQuiz_Id(userId, quizId)
                .map(a -> {
                    a.updateAnswer(chosen, isCorrect);
                    return a;
                })
                .orElseGet(() -> quizAttemptRepository.save(
                        QuizAttempt.builder()
                                .quiz(quiz)
                                .user(userRepository.getReferenceById(userId))
                                .selectedChoice(chosen)
                                .isCorrect(isCorrect)
                                .build()
                ));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        Map<String, Object> resp = new LinkedHashMap<>();
        if (isCorrect) {
            int total = pointService.addPoints(userId, CORRECT_POINT);

            // 퀴즈 정답 제출 시 3 엽전 획득
            coinService.earnCoin(user, CORRECT_POINT);
            resp.put("isCorrect", true);
            resp.put("point", CORRECT_POINT);
            resp.put("totalPoints", total);
        } else {
            resp.put("isCorrect", false);
            resp.put("correctAnswer", quiz.getCorrectChoice().getLabel());
            resp.put("selectedAnswer", selected);
            for (QuizChoice c : quiz.getChoices()) {
                String key = "description" + c.getLabel();
                String desc = (c.getDescription() == null || c.getDescription().isBlank()) ? "" : c.getDescription();
                resp.put(key, desc);
            }
        }
        return resp;
    }
}
