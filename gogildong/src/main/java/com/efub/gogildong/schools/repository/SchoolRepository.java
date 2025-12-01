package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.statistics.dto.DailyCountProjection; // 🔥 추가
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;                                  // 🔥 추가
import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsBySchoolCode(String schoolCode);

    @Query(value = """
    SELECT DISTINCT s.* 
    FROM school s
    LEFT JOIN school_tag st ON s.school_id = st.school_id
    WHERE ST_DWithin(
        s.location,
        ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
        :radiusInMeters
    )
    AND (:tag IS NULL OR st.tag_name = :tag)
    """, nativeQuery = true)
    Page<School> findSchoolsWithinRadiusAndTag(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radiusInMeters") double radiusInMeters,
            @Param("tag") String tag,
            Pageable pageable
    );

    @Query(value = """
        SELECT *
        FROM school
        WHERE school_name ILIKE CONCAT('%', :query, '%')
           OR address ILIKE CONCAT('%', :query, '%')
        ORDER BY
            CASE
                WHEN school_name ILIKE CONCAT(:query, '%') THEN 1
                WHEN school_name ILIKE CONCAT('%', :query, '%') THEN 2
                WHEN address ILIKE CONCAT('% ', :query, ' %') THEN 3
                ELSE 4
            END
        """, nativeQuery = true)
    Page<School> searchByQuery(@Param("query") String query, Pageable pageable);

    Optional<School> findBySchoolCode(String schoolCode);
    Optional<School> findBySchoolId(Long schoolId);

    // 특정 시점까지의 참여 학교 누적 수
    long countByCreatedAtBefore(LocalDateTime before);

    // 기간 내 "새로 참여한 학교"의 일별 개수
    @Query(value = """
            SELECT DATE(s.created_at) AS date, COUNT(*) AS count
            FROM school s
            WHERE s.created_at >= :start AND s.created_at < :end
            GROUP BY DATE(s.created_at)
            """,
            nativeQuery = true)
    List<DailyCountProjection> countDailyNewSchools(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
