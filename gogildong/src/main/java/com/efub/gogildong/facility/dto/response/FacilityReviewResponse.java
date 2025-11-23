package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.FacilityReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class FacilityReviewResponse {
    private Long userId;
    private String userName;
    private Long reviewId;
    private String reviewText;

    public static FacilityReviewResponse from(FacilityReview review) {
        return FacilityReviewResponse.builder()
                .userId(review.getUser().getUserId())
                .userName(review.getUser().getUsername())
                .reviewId(review.getFacilityReviewId())
                .reviewText(review.getReviewText())
                .build();
    }
}
