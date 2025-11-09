package com.efub.gogildong.facility.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FacilityImageListResponse {
    private int total;
    private List<FacilityImageSummaryResponse> reportImages;
}
