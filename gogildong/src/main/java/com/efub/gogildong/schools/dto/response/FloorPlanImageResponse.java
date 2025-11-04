package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.facility.domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FloorPlanImageResponse {
    private Long floorId;
    private String floorName;
    private String floorPlanImage;

    public static FloorPlanImageResponse from(Floor floor) {
        return FloorPlanImageResponse.builder()
                .floorId(floor.getFloorId())
                .floorName(floor.getFloorName())
                .floorPlanImage(floor.getFloorPlanImage())
                .build();
    }
}
