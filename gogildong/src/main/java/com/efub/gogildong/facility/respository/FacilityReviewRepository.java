package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityReviewRepository extends JpaRepository<FacilityReview, Long> {

    Page<FacilityReview> findByFacility(Facility facility, Pageable pageable);

    Optional<FacilityReview> findByFacilityReviewId(Long facilityReviewId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        update FacilityReview fr
        set fr.likeCount = fr.likeCount + 1
        where fr.id = :reviewId
    """)
    int increaseLikeCount(@Param("reviewId") Long reviewId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        update FacilityReview fr
        set fr.likeCount = fr.likeCount - 1
        where fr.id = :reviewId
          and fr.likeCount > 0
    """)
    int decreaseLikeCount(@Param("reviewId") Long reviewId);
}
