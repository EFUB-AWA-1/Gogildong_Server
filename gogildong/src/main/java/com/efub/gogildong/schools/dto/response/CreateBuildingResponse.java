package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBuildingResponse {
    private final Long buildingId;
    private final String buildingName;
}
