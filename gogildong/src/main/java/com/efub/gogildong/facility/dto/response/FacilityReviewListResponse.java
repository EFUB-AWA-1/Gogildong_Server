package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class FacilityReviewListResponse {
    private int total;
    private boolean isLast;
    private String reviewSummary;
    private List<FacilityReviewSummaryResponse> reviews;

    public static FacilityReviewListResponse from(Page<FacilityReview> reviewPage, Facility facility, List<FacilityReviewSummaryResponse> reviews) {
        return FacilityReviewListResponse.builder()
                .total((int) reviewPage.getTotalElements())
                .isLast(reviewPage.isLast())
                .reviewSummary(facility.getReviewSummary())
                .reviews(reviews)
                .build();
    }
}
