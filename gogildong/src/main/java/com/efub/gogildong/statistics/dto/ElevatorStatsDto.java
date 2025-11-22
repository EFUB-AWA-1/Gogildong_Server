package com.efub.gogildong.statistics.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ElevatorStatsDto extends FacilityStatsDto {
    private Float doorWidth;
    private Float minDoorWidth;
    private Float maxDoorWidth;
    private Float doorHeight;
    private Float maxControlPanelHeight;
}
