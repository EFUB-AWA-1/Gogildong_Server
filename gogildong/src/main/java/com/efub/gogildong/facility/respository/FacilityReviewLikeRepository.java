package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewLike;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityReviewLikeRepository extends JpaRepository<FacilityReviewLike, Long> {
    Optional<FacilityReviewLike> findByFacilityReviewLikeId(Long facilityReviewLikeId);

    boolean existsByFacilityReviewAndUser(FacilityReview review, User user);
}
