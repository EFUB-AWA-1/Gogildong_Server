package com.efub.gogildong.reports.dto.request.elevator;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.reports.domain.ElevatorReport;
import com.efub.gogildong.reports.domain.Report;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewElevatorReportRequest {
    private Long floorId;

    @NotBlank(message = "시설 이름을 작성해주세요.")
    private String facilityNickname;

    @NotBlank(message = "시설 사진을 포함해주세요!")
    private String elevatorReportImage;

    private Float doorWidth;

    private Float interiorDepth;

    private Float maxControlPanelHeight;

    private StaffApproved isStaffApproved;

    private Boolean isAvailableDuringClass;

    public static Facility toFacilityEntity(NewElevatorReportRequest request, String facilityName, Floor floor) {
        return Facility.builder()
                .facilityName(facilityName)
                .facilityNickname(request.getFacilityNickname())
                .facilityType(FacilityType.ELEVATOR)
                .floor(floor)
                .build();
    }

    public static ElevatorReport toElevatorReportEntity(NewElevatorReportRequest request, Report report) {
        return ElevatorReport.builder()
                .elevatorReportImage(request.getElevatorReportImage())
                .doorWidth(request.getDoorWidth())
                .interiorDepth(request.getInteriorDepth())
                .maxControlPanelHeight(request.getMaxControlPanelHeight())
                .isStaffApproved(request.getIsStaffApproved())
                .isAvailableDuringClass(request.getIsAvailableDuringClass())
                .build();
    }

    public static Elevator toElevatorEntity(NewElevatorReportRequest request, Facility facility, ElevatorReport report) {
        Elevator elevator = Elevator.builder()
                .interiorDepth(request.getInteriorDepth())
                .doorWidth(request.getDoorWidth())
                .maxControlPanelHeight(request.getMaxControlPanelHeight())
                .facility(facility)
                .maxDoorWidth(request.getDoorWidth())
                .minDoorWidth(request.getDoorWidth())
                .isStaffApproved(request.getIsStaffApproved())
                .isAvailableDuringClass(request.getIsAvailableDuringClass())
                .build();

        elevator.addElevatorReport(report);

        return elevator;
    }
}
