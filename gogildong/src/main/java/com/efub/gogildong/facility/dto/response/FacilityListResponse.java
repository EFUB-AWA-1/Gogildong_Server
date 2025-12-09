package com.efub.gogildong.facility.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FacilityListResponse {
    private List<FacilitySummaryResponse> facilities;
    private int totalElements;
}
