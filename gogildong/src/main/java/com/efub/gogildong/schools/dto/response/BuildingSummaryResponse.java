package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.facility.domain.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuildingSummaryResponse {
    private Long buildingId;
    private String buildingName;

    public static BuildingSummaryResponse from(Building building) {
        return new BuildingSummaryResponse(building.getBuildingId(), building.getBuildingName());
    }
}
