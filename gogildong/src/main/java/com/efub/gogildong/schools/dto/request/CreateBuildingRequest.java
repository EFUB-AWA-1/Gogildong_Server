package com.efub.gogildong.schools.dto.request;

import com.efub.gogildong.facility.domain.Building;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBuildingRequest {
    @NotBlank(message = "건물 이름을 입력해주세요")
    private final String buildingName;

    public Building createBuilding() {
        return new Building(buildingName);
    }
}
