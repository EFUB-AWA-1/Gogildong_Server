package com.efub.gogildong.reports.dto.request.classroom;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.reports.domain.ClassroomReport;
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
public class NewClassroomReportRequest {
    private Long floorId;

    @NotBlank(message = "시설 이름을 작성해주세요.")
    private String facilityName;

    @NotBlank(message = "시설 사진을 포함해주세요!")
    private String classroomReportImage;

    private Float doorWidth;

    private Float doorHeight;

    private Float minAisleWidth;

    private Boolean hasThreshold;

    private DoorType doorType;

    private String note;

    public static Facility toFacilityEntity(NewClassroomReportRequest request, String facilityName, Floor floor) {
        return Facility.builder()
                .facilityName(facilityName)
                .facilityNickname(request.getFacilityName())
                .facilityType(FacilityType.CLASSROOM)
                .floor(floor)
                .build();
    }

    public static ClassroomReport toClassroomReportEntity(NewClassroomReportRequest request, Report report) {
        return ClassroomReport.builder()
                .classroomReportImage(request.getClassroomReportImage())
                .doorWidth(request.getDoorWidth())
                .doorHeight(request.getDoorHeight())
                .minAisleWidth(request.getMinAisleWidth())
                .hasThreshold(request.getHasThreshold())
                .doorType(request.getDoorType())
                .note(request.getNote())
                .build();
    }

    public static Classroom toClassroomEntity(NewClassroomReportRequest request, Facility facility, ClassroomReport report) {
        Classroom classroom = Classroom.builder()
                .doorHeight(request.getDoorHeight())
                .doorWidth(request.getDoorWidth())
                .minAisleWidth(request.getMinAisleWidth())
                .hasThreshold(request.getHasThreshold())
                .doorType(request.getDoorType())
                .facility(facility)
                .maxDoorWidth(request.getDoorWidth())
                .minDoorWidth(request.getDoorWidth())
                .build();

        classroom.addClassroomReport(report);

        return classroom;
    }

}
