package com.efub.gogildong.user.repository;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.statistics.dto.DailyCountProjection;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findBySchoolAndRole(School school, UserRole role);
    Optional<User> findByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByLoginId(String loginId);

    // 월 합계용
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 일별 신규 사용자 수
    @Query(value = """
            SELECT DATE(u.created_at) AS date, COUNT(*) AS count
            FROM users u
            WHERE u.created_at >= :start AND u.created_at < :end
            GROUP BY DATE(u.created_at)
            """, nativeQuery = true)
    List<DailyCountProjection> countDaily(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
