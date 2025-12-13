package com.efub.gogildong.reports.dto.response.elevator;

import com.efub.gogildong.reports.domain.ElevatorReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ElevatorReportSummaryResponse {

    private Float doarWidth;
    private Float doarHeight;
    private Float maxControlPanelHeight;

    public static ElevatorReportSummaryResponse from(ElevatorReport elevatorReport) {

        return ElevatorReportSummaryResponse.builder()
                .doarWidth(elevatorReport.getDoorWidth())
                .doarHeight(elevatorReport.getDoorHeight())
                .maxControlPanelHeight(elevatorReport.getMaxControlPanelHeight())
                .build();
    }


}
