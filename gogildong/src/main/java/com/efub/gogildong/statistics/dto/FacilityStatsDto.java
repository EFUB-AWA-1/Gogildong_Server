package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.facility.domain.FacilityType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class FacilityStatsDto {

    private Long facilityId;
    private FacilityType facilityType;   // RESTROOM | ELEVATOR | CLASSROOM
    private Long schoolId;
    private String schoolName;
    private String buildingName;
    private String floorName;

    private String region;
    private LocalDateTime lastActivityAt;
}
