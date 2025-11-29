package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.statistics.dto.DailyCountProjection;
import com.efub.gogildong.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolViewRequestRepository extends JpaRepository<SchoolViewRequest, Long> {
    Optional<SchoolViewRequest> findBySchoolAndUser(School school, User user);
    Optional<SchoolViewRequest> findByRequestId(Long requestId);
    List<SchoolViewRequest> findAllByOrderByRequestedAtDesc();

    // 월 합계용
    long countByRequestedAtBetween(LocalDateTime start, LocalDateTime end);

    // 일별 열람 요청 수
    @Query(value = """
            SELECT DATE(svr.requested_at) AS date, COUNT(*) AS count
            FROM school_view_request svr
            WHERE svr.requested_at >= :start AND svr.requested_at < :end
            GROUP BY DATE(svr.requested_at)
            """, nativeQuery = true)
    List<DailyCountProjection> countDaily(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
