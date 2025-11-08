package com.efub.gogildong.quiz.dto.response;

import java.util.List;

public record SubmitQuizResponse(
        Long attempt_id,
        Long quiz_id,
        Long user_id,
        boolean isCorrect,
        String status,
        String finishedAt,
        int point,
        List<WrongAnswerItem> answers // 오답일 때만 사용
) {
    public record WrongAnswerItem(
            Long quiz_id,
            String question,
            String selected_answer,
            String correct_answer,
            boolean is_correct,
            String explanation
    ) {}
}
