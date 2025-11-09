package com.efub.gogildong.reports.dto.request;

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

    private Float doorWidth;

    private Float doorHeight;

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
                .doorWidth(request.getDoorWidth())
                .doorHeight(request.getDoorHeight())
                .toiletHeight(request.getToiletHeight())
                .grabBar(request.getGrabBar())
                .build();
    }

    public static Restroom toRestroomEntity(NewRestRoomReportRequest request, Facility facility, RestRoomReport report) {
        Restroom restroom = Restroom.builder()
                .isAccessible(request.getIsAccessible())
                .doorHeight(request.getDoorHeight())
                .doorType(request.getDoorType())
                .doorWidth(request.getDoorWidth())
                .toiletHeight(request.getToiletHeight())
                .facility(facility)
                .gender(request.getGender())
                .maxDoorWidth(request.getDoorWidth())
                .minDoorWidth(request.getDoorWidth())
                .grabBar(request.getGrabBar())
                .build();

        restroom.addRestRoomReport(report);

        return restroom;
    }
}
