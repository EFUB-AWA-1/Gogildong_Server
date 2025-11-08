package com.efub.gogildong.quiz.dto.response;

import com.efub.gogildong.quiz.dto.QuizListItemDto;
import java.util.List;

public record QuizListResponse(List<QuizListItemDto> quizzes) {}
