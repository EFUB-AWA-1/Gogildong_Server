package com.efub.gogildong.reports.dto.response.etc;

import com.efub.gogildong.reports.domain.EtcReport;
import com.efub.gogildong.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtcReportResponse {

    private Long reportId;
    private Long etcReportId;
    private UserResponseDto user;
    private String etcReportImage;
    private String note;

    public static EtcReportResponse from(EtcReport etcReport) {
        return EtcReportResponse.builder()
                .reportId(etcReport.getReport().getReportId())
                .etcReportId(etcReport.getEtcReportId())
                .user(UserResponseDto.from(etcReport.getReport().getUser()))
                .etcReportImage(etcReport.getEtcReportImage())
                .note(etcReport.getNote())
                .build();
    }
}
