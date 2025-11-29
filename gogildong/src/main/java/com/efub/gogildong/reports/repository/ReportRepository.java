package com.efub.gogildong.reports.repository;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.statistics.dto.DailyCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByOrderByCreatedAtDesc();
    List<Report> findAllByFacility(Facility facility);
    Optional<Report> findByReportId(Long reportId);

    // 기간 내 제보 수
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // 기간 내 시설 타입(교실/화장실/엘리베이터)별 제보 수
    @Query("""
           SELECT r.reportType AS reportType, COUNT(r) AS count
           FROM Report r
           WHERE r.createdAt >= :start AND r.createdAt < :end
           GROUP BY r.reportType
           """)
    List<FacilityTypeCountProjection> countByReportTypeBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    interface FacilityTypeCountProjection {
        FacilityType getReportType();
        long getCount();
    }

    // 기간 내 일별 제보 수
    @Query(value = """
            SELECT DATE(r.created_at) AS date, COUNT(*) AS count
            FROM report r
            WHERE r.created_at >= :start AND r.created_at < :end
            GROUP BY DATE(r.created_at)
            """,
            nativeQuery = true)
    List<DailyCountProjection> countDaily(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
