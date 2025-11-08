package com.efub.gogildong.quiz.dto;

public record QuizListItemDto(
        Long quiz_id,
        String title,
        String attemptStatus,
        Boolean isCorrect
) {}
