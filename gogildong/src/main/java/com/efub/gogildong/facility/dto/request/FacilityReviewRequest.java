package com.efub.gogildong.facility.dto.request;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityReviewRequest {
    private Long facilityId;
    private String reviewText;

    public FacilityReview toEntity(Facility facility, User user) {
        return FacilityReview.builder()
                .facility(facility)
                .user(user)
                .reviewText(reviewText)
                .likeCount(0)
                .commentCount(0)
                .build();
    }
}
