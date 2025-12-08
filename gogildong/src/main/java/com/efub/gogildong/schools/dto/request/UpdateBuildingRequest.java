package com.efub.gogildong.schools.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateBuildingRequest {
    @NotNull
    private Long buildingId;

    @NotBlank
    private String buildingName;
}
