package com.efub.gogildong.facility.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateFloorPlanImageRequest {
    @NotNull
    Long floorId;
    @NotBlank
    String floorPlanImage;
    @NotBlank
    String floorName;
}
