package com.efub.gogildong.reports.dto.request.elevator;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.reports.domain.ElevatorReport;
import com.efub.gogildong.reports.domain.Report;
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
    private String facilityName;

    @NotBlank(message = "시설 사진을 포함해주세요!")
    private String elevatorReportImage;

    private Float doorWidth;

    private Float doorHeight;

    private Float maxControlPanelHeight;

    private String note;

    public static Facility toFacilityEntity(NewElevatorReportRequest request, String facilityName, Floor floor) {
        return Facility.builder()
                .facilityName(facilityName)
                .facilityNickname(request.getFacilityName())
                .facilityType(FacilityType.ELEVATOR)
                .floor(floor)
                .build();
    }

    public static ElevatorReport toElevatorReportEntity(NewElevatorReportRequest request, Report report) {
        return ElevatorReport.builder()
                .elevatorReportImage(request.getElevatorReportImage())
                .doorWidth(request.getDoorWidth())
                .doorHeight(request.getDoorHeight())
                .maxControlPanelHeight(request.getMaxControlPanelHeight())
                .note(request.getNote())
                .build();
    }

    public static Elevator toElevatorEntity(NewElevatorReportRequest request, Facility facility, ElevatorReport report) {
        Elevator elevator = Elevator.builder()
                .doorHeight(request.getDoorHeight())
                .doorWidth(request.getDoorWidth())
                .maxControlPanelHeight(request.getMaxControlPanelHeight())
                .facility(facility)
                .maxDoorWidth(request.getDoorWidth())
                .minDoorWidth(request.getDoorWidth())
                .build();

        elevator.addElevatorReport(report);

        return elevator;
    }
}
