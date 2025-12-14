package com.efub.gogildong.facility.respository;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewLike;
import com.efub.gogildong.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityReviewLikeRepository extends JpaRepository<FacilityReviewLike, Long> {
    boolean existsByFacilityReviewAndUser(FacilityReview review, User user);

    Optional<FacilityReviewLike> findByFacilityReviewAndUser(FacilityReview facilityReview, User user);

    @Query("""
    select frl.facilityReview.facilityReviewId
    from FacilityReviewLike frl
    where frl.user = :user
      and frl.facilityReview in :reviews
""")
    List<Long> findLikedReviewIdsByUserAndReviews(
            @Param("user") User user,
            @Param("reviews") List<FacilityReview> reviews
    );
}
