package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Restroom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestroomResponse {
    private FacilityDetailResponse facilityDetail;
    private Float doorWidth;
    private String gender;
    private boolean isAccessible;
    private String doorType;

    public static RestroomResponse from(Restroom restroom) {
        Facility facility = restroom.getFacility();
        return RestroomResponse.builder()
                .facilityDetail(FacilityDetailResponse.from(facility))
                .doorWidth(Math.min(restroom.getEntranceDoorWidth(), restroom.getInnerDoorWidth()))
                .gender(restroom.getGender().name())
                .isAccessible(restroom.getIsAccessible())
                .doorType(restroom.getDoorType().name())
                .build();
    }
}
