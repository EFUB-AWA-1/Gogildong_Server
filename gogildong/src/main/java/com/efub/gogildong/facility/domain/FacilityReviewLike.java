package com.efub.gogildong.facility.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "facility_review_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_facility_review_like_review_user",
                columnNames = {"facility_review_id", "user_id"}
        )
)
public class FacilityReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityReviewLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_review_id", nullable = false)
    private FacilityReview facilityReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public FacilityReviewLike(FacilityReview facilityReview, User user) {
        this.facilityReview = facilityReview;
        this.user = user;
    }
}
