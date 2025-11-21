package com.efub.gogildong.quiz.service;

import com.efub.gogildong.quiz.domain.Quiz;
import com.efub.gogildong.quiz.dto.AttemptBrief;
import com.efub.gogildong.quiz.dto.ChoiceDto;
import com.efub.gogildong.quiz.dto.QuizListItemDto;
import com.efub.gogildong.quiz.dto.response.QuizDetailResponse;
import com.efub.gogildong.quiz.dto.response.QuizListResponse;
import com.efub.gogildong.quiz.repository.QuizAttemptRepository;
import com.efub.gogildong.quiz.repository.QuizChoiceRepository;
import com.efub.gogildong.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizQueryService {

    private final QuizRepository quizRepository;
    private final QuizChoiceRepository quizChoiceRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    // GET /quiz : 전체 퀴즈 목록
    public QuizListResponse getAllQuizzes(Long userId) {
        List<QuizListItemDto> items = quizRepository.findAllWithAttempt(userId);
        return new QuizListResponse(items);
    }

    // GET /quiz/{quizId} : 퀴즈 상세
    public QuizDetailResponse getQuizDetail(Long quizId, Long userId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));

        List<ChoiceDto> choices = quizChoiceRepository.findDtosByQuizId(quizId);

        AttemptBrief attempt = quizAttemptRepository.findByUser_UserIdAndQuiz_Id(userId, quizId)
                .map(a -> new AttemptBrief("SUBMITTED",
                        a.getSelectedChoice().getId(),
                        a.getIsCorrect()))
                .orElse(new AttemptBrief("NONE", null, null));

        return new QuizDetailResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getQuestion(),
                choices,
                attempt
        );
    }
}
