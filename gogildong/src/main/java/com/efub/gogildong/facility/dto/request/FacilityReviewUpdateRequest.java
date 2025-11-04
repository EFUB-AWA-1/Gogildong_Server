package com.efub.gogildong.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityReviewUpdateRequest {
    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String reviewText;
}
