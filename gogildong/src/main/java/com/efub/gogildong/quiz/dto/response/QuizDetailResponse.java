package com.efub.gogildong.quiz.dto.response;

import com.efub.gogildong.quiz.dto.AttemptBrief;
import com.efub.gogildong.quiz.dto.ChoiceDto;

import java.util.List;

public record QuizDetailResponse(
        Long quiz_id,
        String title,
        String question,
        List<ChoiceDto> choices,
        AttemptBrief attempt
) {}
