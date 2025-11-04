package com.efub.gogildong.facility.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FacilityReviewCommentListResponse {
    private int total;
    private List<FacilityReviewCommentResponse> reviewComments;

    public static FacilityReviewCommentListResponse from(List<FacilityReviewCommentResponse> comments) {
        return FacilityReviewCommentListResponse.builder()
                .total(comments.size())
                .reviewComments(comments)
                .build();
    }
}
