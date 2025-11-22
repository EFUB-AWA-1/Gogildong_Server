package com.efub.gogildong.statistics.dto.request;

import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.ReportStatus;
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
    private String schoolLevel;
    private Integer minStudentCount;

    // facility
    private List<FacilityType> facilityType;
    private List<Long> buildingId;
    private List<Long> floorId;

    // restroom
    private List<DoorType> doorType;
    private Integer doorWidthMin;
    private Integer doorHeightMin;

    // report
    private List<ReportStatus> reportStatus;
    private List<FacilityType> reportType;
    private Boolean isPublic;

    // readRequest
    private List<RequestStatus> readRequestStatus;
    private List<FacilityType> readRequestType;
    private List<String> reason;

    private List<String> columns;
}
