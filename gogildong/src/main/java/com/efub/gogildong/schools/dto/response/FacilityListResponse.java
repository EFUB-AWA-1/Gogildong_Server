package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FacilityListResponse {
    private int totalElements;
    private List<FacilitySummaryResponse> facility;
}
