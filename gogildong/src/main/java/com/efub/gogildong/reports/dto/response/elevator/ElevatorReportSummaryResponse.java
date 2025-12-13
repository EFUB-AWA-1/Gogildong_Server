package com.efub.gogildong.reports.dto.response.elevator;

import com.efub.gogildong.reports.domain.ElevatorReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ElevatorReportSummaryResponse {

    private Float doarWidth;
    private Float interiorDepth;
    private Float maxControlPanelHeight;

    public static ElevatorReportSummaryResponse from(ElevatorReport elevatorReport) {

        return ElevatorReportSummaryResponse.builder()
                .doarWidth(elevatorReport.getDoorWidth())
                .interiorDepth(elevatorReport.getInteriorDepth())
                .maxControlPanelHeight(elevatorReport.getMaxControlPanelHeight())
                .build();
    }


}
