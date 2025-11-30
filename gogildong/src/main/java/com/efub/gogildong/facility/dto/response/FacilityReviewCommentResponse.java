package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.FacilityReviewComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FacilityReviewCommentResponse {
    private Long userId;
    private String userName;
    private Long commentId;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FacilityReviewCommentResponse from(FacilityReviewComment comment) {
        return FacilityReviewCommentResponse.builder()
                .userId(comment.getUser().getUserId())
                .userName(comment.getUser().getUsername())
                .commentId(comment.getFacilityReviewCommentId())
                .commentText(comment.getCommentText())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
