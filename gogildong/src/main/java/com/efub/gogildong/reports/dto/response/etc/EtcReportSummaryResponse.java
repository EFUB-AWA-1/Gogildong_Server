package com.efub.gogildong.reports.dto.response.etc;

import com.efub.gogildong.reports.domain.EtcReport;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtcReportSummaryResponse {

    private String note;

    public static EtcReportSummaryResponse from(EtcReport etcReport) {
        return EtcReportSummaryResponse.builder()
                .note(etcReport.getNote())
                .build();
    }
}
