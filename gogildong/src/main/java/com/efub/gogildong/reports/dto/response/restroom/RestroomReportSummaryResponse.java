package com.efub.gogildong.reports.dto.response.restroom;

import com.efub.gogildong.reports.domain.RestRoomReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestroomReportSummaryResponse {

    private Float entranceDoorWidth;
    private Float entranceDoorHeight;
    private Float innerDoorWidth;
    private Float innerDoorHeight;
    private Float toiletHeight;

    public static RestroomReportSummaryResponse from(RestRoomReport restRoomReport) {

        return RestroomReportSummaryResponse.builder()
                .entranceDoorWidth(restRoomReport.getEntranceDoorWidth())
                .entranceDoorHeight(restRoomReport.getEntranceDoorHeight())
                .innerDoorWidth(restRoomReport.getInnerDoorWidth())
                .innerDoorHeight(restRoomReport.getInnerDoorHeight())
                .toiletHeight(restRoomReport.getToiletHeight())
                .build();
    }
}
