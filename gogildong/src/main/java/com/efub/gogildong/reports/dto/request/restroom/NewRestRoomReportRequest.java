package com.efub.gogildong.reports.dto.request.restroom;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.reports.domain.RestRoomReport;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewRestRoomReportRequest {
    private Long floorId;

    @NotBlank(message = "시설 이름을 작성해주세요.")
    private String facilityName;

    @NotBlank(message = "시설 사진을 포함해주세요!")
    private String restRoomReportImage;

    private GenderType gender;

    private Boolean isAccessible;

    private DoorType doorType;

    private Float entranceDoorWidth;

    private Float entranceDoorHeight;

    private Float innerDoorWidth;

    private Float innerDoorHeight;

    private Float toiletHeight;

    private Boolean grabBar;

    public static Facility toFacilityEntity(NewRestRoomReportRequest request, String facilityName, Floor floor) {
        return Facility.builder()
                .facilityName(facilityName)
                .facilityNickname(request.getFacilityName())
                .facilityType(FacilityType.RESTROOM)
                .floor(floor)
                .build();
    }

    public static RestRoomReport toRestRoomReportEntity(NewRestRoomReportRequest request, Report report) {
        return RestRoomReport.builder()
                .restroomReportImage(request.getRestRoomReportImage())
                .gender(request.getGender())
                .doorType(request.getDoorType())
                .isAccessible(request.getIsAccessible())
                .entranceDoorWidth(request.getEntranceDoorWidth())
                .entranceDoorHeight(request.getEntranceDoorHeight())
                .innerDoorWidth(request.getInnerDoorWidth())
                .innerDoorHeight(request.getInnerDoorHeight())
                .toiletHeight(request.getToiletHeight())
                .grabBar(request.getGrabBar())
                .build();
    }

    public static Restroom toRestroomEntity(NewRestRoomReportRequest request, Facility facility, RestRoomReport report) {
        Restroom restroom = Restroom.builder()
                .isAccessible(request.getIsAccessible())
                .doorType(request.getDoorType())
                .entranceDoorWidth(request.getEntranceDoorWidth())
                .entranceDoorHeight(request.getEntranceDoorHeight())
                .innerDoorWidth(request.getInnerDoorWidth())
                .innerDoorHeight(request.getInnerDoorHeight())
                .minEntranceDoorWidth(report.getEntranceDoorWidth())
                .maxEntranceDoorWidth(report.getEntranceDoorWidth())
                .maxInnerDoorWidth(report.getInnerDoorWidth())
                .minInnerDoorWidth(report.getInnerDoorWidth())
                .toiletHeight(request.getToiletHeight())
                .facility(facility)
                .gender(request.getGender())
                .grabBar(request.getGrabBar())
                .build();

        restroom.addRestRoomReport(report);

        return restroom;
    }
}
