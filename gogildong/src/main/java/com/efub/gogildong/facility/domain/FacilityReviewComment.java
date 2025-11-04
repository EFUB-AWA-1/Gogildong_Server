package com.efub.gogildong.facility.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FacilityReviewComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityReviewCommentId;

    @Column(nullable = false)
    private String commentText;

    // 리뷰와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_review_id", nullable = false)
    private FacilityReview facilityReview;

    @Builder
    public FacilityReviewComment(Long facilityReviewCommentId, String commentText) {
        this.facilityReviewCommentId = facilityReviewCommentId;
        this.commentText = commentText;
    }

    public void setFacilityReview(FacilityReview facilityReview) {
        this.facilityReview = facilityReview;
    }
}
