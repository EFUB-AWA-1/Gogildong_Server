package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.facility.domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FloorResponse {
    private long floorId;
    private String floorName;

    public static FloorResponse from(final Floor floor) {
        return new FloorResponse(floor.getFloorId(), floor.getFloorName());
    }
}
