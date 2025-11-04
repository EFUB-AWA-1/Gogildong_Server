package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.FacilityReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FacilityReviewSummaryResponse {
    private Long userId;
    private String userName;
    private Long reviewId;
    private String reviewText;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;

    public static FacilityReviewSummaryResponse from(FacilityReview review) {
        return FacilityReviewSummaryResponse.builder()
                .userId(review.getUser().getUserId())
                .userName(review.getUser().getUsername())
                .reviewId(review.getFacilityReviewId())
                .reviewText(review.getReviewText())
                .likeCount(review.getLikeCount())
                .commentCount(review.getCommentCount())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
