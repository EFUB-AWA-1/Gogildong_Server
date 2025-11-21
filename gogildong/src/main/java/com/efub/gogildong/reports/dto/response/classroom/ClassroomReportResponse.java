package com.efub.gogildong.reports.dto.response.classroom;

import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.reports.domain.ClassroomReport;
import com.efub.gogildong.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClassroomReportResponse {

    private Long reportId;
    private Long classroomReportId;
    private UserResponseDto user;
    private String classroomReportImage;
    private Float doorWidth;
    private Float doorHeight;
    private Float minAisleWidth;
    private Boolean hasThreshold;
    private DoorType doorType;
    private String note;

    public static ClassroomReportResponse from(ClassroomReport classroomReport) {
        return ClassroomReportResponse.builder()
                .reportId(classroomReport.getReport().getReportId())
                .classroomReportId(classroomReport.getClassroomReportId())
                .user(UserResponseDto.from(classroomReport.getReport().getUser()))
                .classroomReportImage(classroomReport.getClassroomReportImage())
                .doorWidth(classroomReport.getDoorWidth())
                .doorHeight(classroomReport.getDoorHeight())
                .minAisleWidth(classroomReport.getMinAisleWidth())
                .hasThreshold(classroomReport.getHasThreshold())
                .doorType(classroomReport.getDoorType())
                .note(classroomReport.getNote())
                .build();
    }
}
