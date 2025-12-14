package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Elevator;
import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.StaffApproved;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ElevatorResponse {
    private FacilityDetailResponse facilityDetail;
    private StaffApproved isStaffApproved;
    private Boolean isAvailableDuringClass;

    public static ElevatorResponse from(Elevator elevator) {
        Facility facility = elevator.getFacility();
        return ElevatorResponse.builder()
                .facilityDetail(FacilityDetailResponse.from(facility))
                .isStaffApproved(elevator.getIsStaffApproved())
                .isAvailableDuringClass(elevator.getIsAvailableDuringClass())
                .build();
    }
}
