package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.facility.domain.DoorType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ClassroomStatsDto extends FacilityStatsDto {
    private Float doorWidth;
    private Float minDoorWidth;
    private Float maxDoorWidth;
    private Float doorHeight;
    private Float minAisleWidth;
    private Boolean hasThreshold;
    private DoorType doorType;
}
