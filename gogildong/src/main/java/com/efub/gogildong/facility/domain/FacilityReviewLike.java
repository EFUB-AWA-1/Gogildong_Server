package com.efub.gogildong.facility.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FacilityReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityReviewLikeId;

    // 리뷰와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_review_id", nullable = false)
    private FacilityReview facilityReview;

    @Builder
    public FacilityReviewLike(Long facilityReviewLikeId) {
        this.facilityReviewLikeId = facilityReviewLikeId;
    }

    public void setFacilityReview(FacilityReview facilityReview) {
        this.facilityReview = facilityReview;
    }
}
