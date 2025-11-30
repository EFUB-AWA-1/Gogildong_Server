package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.FacilityReviewComment;
import com.efub.gogildong.facility.domain.FacilityReviewCommentFlag;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityReviewCommentFlagRepository extends JpaRepository<FacilityReviewCommentFlag, Long> {
    boolean existsByUserAndComment(User user, FacilityReviewComment comment);
}
