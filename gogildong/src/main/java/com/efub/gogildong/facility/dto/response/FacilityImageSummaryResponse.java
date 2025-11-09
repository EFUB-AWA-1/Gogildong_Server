package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.reports.domain.Report;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FacilityImageSummaryResponse {
    private Long userId;
    private String userName;
    private Long reportId;
    private String facilityImage;
    private LocalDateTime createdAt;

    public static FacilityImageSummaryResponse from(Report report) {
        String facilityImage = null;

        // reportType으로 분기
        switch (report.getReportType()) {
            case RESTROOM:
                facilityImage = report.getRestRoomReport().getRestroomReportImage();
                break;
        }

        return FacilityImageSummaryResponse.builder()
                .userId(report.getUser().getUserId())
                .userName(report.getUser().getUsername())
                .reportId(report.getReportId())
                .facilityImage(facilityImage)
                .build();
    }
}
