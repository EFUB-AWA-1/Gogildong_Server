package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.FacilityReview;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FacilityReviewCommentListResponse {
    private int total;
    private FacilityReviewResponse review;
    private List<FacilityReviewCommentResponse> reviewComments;

    public static FacilityReviewCommentListResponse from(FacilityReview review, List<FacilityReviewCommentResponse> comments) {
        return FacilityReviewCommentListResponse.builder()
                .total(comments.size())
                .review(FacilityReviewResponse.from(review))
                .reviewComments(comments)
                .build();
    }
}
