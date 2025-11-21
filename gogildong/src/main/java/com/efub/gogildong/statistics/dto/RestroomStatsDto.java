package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.facility.domain.DoorType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RestroomStatsDto extends FacilityStatsDto {

    private DoorType doorType;
    private Float doorWidth;
    private Float doorHeight;
    private Boolean grabBar;
    private Boolean isAccessible;
}
