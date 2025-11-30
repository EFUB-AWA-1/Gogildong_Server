package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewFlag;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityReviewFlagRepository extends JpaRepository<FacilityReviewFlag, Long> {
    boolean existsByUserAndReview(User user, FacilityReview review);
}
