package com.efub.gogildong.statistics.dto.request;

import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.ReportStatus;
import com.efub.gogildong.schools.domain.EduLevel;
import com.efub.gogildong.schools.domain.RequestStatus;
import lombok.Data;

import java.util.List;

@Data
public class StatisticsFilterRequest {

    // 공통
    private String from;
    private String to;
    private List<String> region;
    private List<Long> schoolIds;

    // school
    private EduLevel schoolLevel;
    private Integer minStudentCount;

    // facility
    private List<FacilityType> facilityType;
    private List<Long> buildingId;
    private List<Long> floorId;

    // restroom
    private List<DoorType> restroomDoorType;
    private Integer restroomDoorWidthMin;
    private Integer restroomDoorHeightMin;

    // elevator
    private Integer elevatorDoorHeightMin;
    private Integer elevatorDoorWidthMin;
    private Integer maxControlPanelHeightMax;

    // classroom
    private Integer classroomDoorHeightMin;
    private Integer classroomDoorWidthMin;
    private Integer minAisleWidthMin;
    private Boolean hasThreshold;

    // report
    private List<ReportStatus> reportStatus;
    private List<FacilityType> reportType;
    private Boolean isPublic;

    // readRequest
    private List<RequestStatus> readRequestStatus;
    private List<String> reason;

    private List<String> columns;
}
