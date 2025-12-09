package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FacilitySummaryResponse {
    private Long facilityId;
    private String facilityName;
    private String facilityNickname;

    public static FacilitySummaryResponse from(final Facility facility) {
        return new FacilitySummaryResponse(facility.getFacilityId(), facility.getFacilityName(), facility.getFacilityNickname());
    }
}
