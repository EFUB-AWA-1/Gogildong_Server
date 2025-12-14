package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Classroom;
import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.facility.domain.Facility;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClassroomResponse {
    private FacilityDetailResponse facilityDetail;
    private Boolean hasThreshold;
    private DoorType doorType;

    public static ClassroomResponse from(Classroom classroom) {
        Facility facility = classroom.getFacility();
        return ClassroomResponse.builder()
                .facilityDetail(FacilityDetailResponse.from(facility))
                .hasThreshold(classroom.getHasThreshold())
                .doorType(classroom.getDoorType())
                .build();
    }
}
