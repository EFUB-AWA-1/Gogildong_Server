package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.FacilityReviewLike;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityReviewLikeResponse {
    private Long likeId;

    public static FacilityReviewLikeResponse from(FacilityReviewLike like) {
        return FacilityReviewLikeResponse.builder().likeId(like.getFacilityReviewLikeId()).build();
    }
}
