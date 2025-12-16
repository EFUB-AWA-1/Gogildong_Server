package com.efub.gogildong.schools.dto.request;

import com.efub.gogildong.facility.domain.Floor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFloorRequest {
    @NotBlank
    private Long buildingId;
    @NotBlank
    private String floorName;
    @NotBlank
    private String floorPlanImage;

    public Floor toEntity() {
        return Floor.builder()
                .floorName(floorName)
                .floorPlanImage(floorPlanImage)
                .build();
    }
}
