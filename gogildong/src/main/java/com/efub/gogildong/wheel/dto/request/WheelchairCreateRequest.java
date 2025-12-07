package com.efub.gogildong.wheel.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WheelchairCreateRequest {

    @NotBlank
    private String wheelChairName;

    @Min(1)
    private int width;

    @Min(0)
    private int maxThreshold;
}
