package com.efub.gogildong.reports.dto.response.elevator;

import com.efub.gogildong.reports.domain.ElevatorReport;
import com.efub.gogildong.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ElevatorReportResponse {
    private Long reportId;
    private Long elevatorReportId;
    private UserResponseDto user;
    private String elevatorReportImage;
    private Float doorWidth;
    private Float doorHeight;
    private Float maxControlPanelHeight;
    private String note;

    public static ElevatorReportResponse from(ElevatorReport elevatorReport) {
        return ElevatorReportResponse.builder()
                .reportId(elevatorReport.getReport().getReportId())
                .elevatorReportId(elevatorReport.getElevatorReportId())
                .user(UserResponseDto.from(elevatorReport.getReport().getUser()))
                .elevatorReportImage(elevatorReport.getElevatorReportImage())
                .doorWidth(elevatorReport.getDoorWidth())
                .doorHeight(elevatorReport.getDoorHeight())
                .maxControlPanelHeight(elevatorReport.getMaxControlPanelHeight())
                .note(elevatorReport.getNote())
                .build();
    }
}
