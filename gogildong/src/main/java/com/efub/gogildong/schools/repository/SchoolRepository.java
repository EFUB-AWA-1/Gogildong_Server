package com.efub.gogildong.schools.repository;

import com.efub.gogildong.schools.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsBySchoolCode(String schoolCode);

    /*
    * 위도, 경도 기준으로 태그에 해당되는 반경 이내 학교를 반환합니다.
    * */
    @Query(value = """
    SELECT DISTINCT s.*\s
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

    /*
    * 검색어를 기준으로 학교를 반환합니다.
    * */
    @Query(value = """
                    SELECT *
                    FROM school
                    WHERE school_name ILIKE %:query%
                    OR address ILIKE %:query%
                    ORDER BY
                    CASE WHEN school_name ILIKE :query||'%' THEN 1
                    WHEN school_name ILIKE '%'||:query||'%' THEN 2
                    WHEN address ILIKE '% '||:query||' %' THEN 3
                    ELSE 4 END
                    """, nativeQuery = true)
   Page<School> searchByQuery(@Param("query") String query, Pageable pageable);

}
