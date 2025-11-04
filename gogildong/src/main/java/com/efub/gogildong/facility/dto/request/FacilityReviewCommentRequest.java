package com.efub.gogildong.facility.dto.request;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewComment;
import com.efub.gogildong.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityReviewCommentRequest {
    private String commentText;

    public FacilityReviewComment toEntity(FacilityReview facilityReview, User user) {
        return FacilityReviewComment.builder()
                .facilityReview(facilityReview)
                .user(user)
                .commentText(commentText)
                .build();
    }
}
