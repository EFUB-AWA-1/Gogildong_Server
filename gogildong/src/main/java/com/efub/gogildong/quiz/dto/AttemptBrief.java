package com.efub.gogildong.quiz.dto;

public record AttemptBrief(
        String attemptStatus,
        Long selectedChoiceId,
        Boolean isCorrect
) {}
