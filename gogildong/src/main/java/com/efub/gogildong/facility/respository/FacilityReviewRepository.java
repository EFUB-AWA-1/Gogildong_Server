package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityReviewRepository extends JpaRepository<FacilityReview, Long> {

    Page<FacilityReview> findByFacility(Facility facility, Pageable pageable);

    Optional<FacilityReview> findByFacilityReviewId(Long facilityReviewId);
}
