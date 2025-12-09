package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.facility.domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class FloorPlanResponse {
    private Long floorId;
    private String floorName;
    private String floorPlanImage;

    public static FloorPlanResponse to(Floor floor) {
        return FloorPlanResponse.builder()
                .floorId(floor.getFloorId())
                .floorName(floor.getFloorName())
                .floorPlanImage(floor.getFloorPlanImage())
                .build();
    }
}
