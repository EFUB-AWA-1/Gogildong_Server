package com.efub.gogildong.quiz.repository;

import com.efub.gogildong.quiz.domain.Quiz;
import com.efub.gogildong.quiz.dto.QuizListItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 목록 조회: 응시 레코드 존재 여부로 attemptStatus/isCorrect 계산
    @Query("""
    select new com.efub.gogildong.quiz.dto.QuizListItemDto(
      q.id,
      q.title,
      case when a.id is null then 'NONE' else 'SUBMITTED' end,
      a.isCorrect
    )
    from Quiz q
    left join QuizAttempt a
      on a.quiz.id = q.id and a.user.id = :userId
    order by q.id asc
    """)
    List<QuizListItemDto> findAllWithAttempt(@Param("userId") Long userId);
}
