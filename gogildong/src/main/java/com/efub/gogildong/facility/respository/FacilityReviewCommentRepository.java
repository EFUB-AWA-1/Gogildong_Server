package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.FacilityReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityReviewCommentRepository extends JpaRepository<FacilityReviewComment, Long> {
    List<FacilityReviewComment> findAllByFacilityReview_FacilityReviewId(Long facilityReviewId);

    Optional<FacilityReviewComment> findByFacilityReviewCommentId(Long facilityReviewCommentId);
}
