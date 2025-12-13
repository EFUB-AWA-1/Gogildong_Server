package com.efub.gogildong.reports.dto.response.classroom;

import com.efub.gogildong.reports.domain.ClassroomReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClassroomReportSummaryResponse {

    private Float doorWidth;
    private Float doorHandleHeight;
    private Float minAisleWidth;

    public static ClassroomReportSummaryResponse from(ClassroomReport classroomReport){
        return ClassroomReportSummaryResponse.builder()
                .doorWidth(classroomReport.getDoorWidth())
                .doorHandleHeight(classroomReport.getDoorHandleHeight())
                .minAisleWidth(classroomReport.getMinAisleWidth())
                .build();
    }

}
