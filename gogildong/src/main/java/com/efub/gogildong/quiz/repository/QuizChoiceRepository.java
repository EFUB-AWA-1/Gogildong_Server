package com.efub.gogildong.quiz.repository;

import com.efub.gogildong.quiz.domain.QuizChoice;
import com.efub.gogildong.quiz.dto.ChoiceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuizChoiceRepository extends JpaRepository<QuizChoice, Long> {

    @Query("""
    select new com.efub.gogildong.quiz.dto.ChoiceDto(c.id, c.label, c.text)
    from QuizChoice c
    where c.quiz.id = :quizId
    order by c.label asc
    """)
    List<ChoiceDto> findDtosByQuizId(@Param("quizId") Long quizId);

    Optional<QuizChoice> findByQuiz_IdAndLabel(Long quizId, String label);
}
