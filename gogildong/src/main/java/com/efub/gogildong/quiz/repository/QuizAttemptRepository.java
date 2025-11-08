package com.efub.gogildong.quiz.repository;

import com.efub.gogildong.quiz.domain.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByUser_UserIdAndQuiz_Id(Long userId, Long quizId);
}
