package com.efub.gogildong.reports.dto.response.restroom;

import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.facility.domain.GenderType;
import com.efub.gogildong.reports.domain.RestRoomReport;
import com.efub.gogildong.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestRoomReportResponse {

    private Long reportId;
    private Long restroomReportId;
    private UserResponseDto user;
    private String restroomReportImage;
    private GenderType gender;
    private Boolean isAccessible;
    private DoorType doorType;
    private Float doorWidth;
    private Float doorHeight;
    private Float toiletHeight;
    private Boolean grabBar;

    public static RestRoomReportResponse from(RestRoomReport restRoomReport) {
        return RestRoomReportResponse.builder()
                .reportId(restRoomReport.getReport().getReportId())
                .restroomReportId(restRoomReport.getRestroomReportId())
                .user(UserResponseDto.from(restRoomReport.getReport().getUser()))
                .restroomReportImage(restRoomReport.getRestroomReportImage())
                .gender(restRoomReport.getGender())
                .isAccessible(restRoomReport.getIsAccessible())
                .doorType(restRoomReport.getDoorType())
                .doorWidth(restRoomReport.getDoorWidth())
                .doorHeight(restRoomReport.getDoorHeight())
                .toiletHeight(restRoomReport.getToiletHeight())
                .grabBar(restRoomReport.getGrabBar())
                .build();
    }

}
