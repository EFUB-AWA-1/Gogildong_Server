package com.efub.gogildong.reports.dto.summary;

import com.efub.gogildong.reports.domain.ReportFlag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportFlagSummary {

    private Long reportFlagId;
    private String reason;
    private LocalDateTime createdAt;

    public static ReportFlagSummary of(ReportFlag reportFlag) {
        return ReportFlagSummary.builder()
                .reportFlagId(reportFlag.getReportFlagId())
                .reason(reportFlag.getReason())
                .createdAt(reportFlag.getCreatedAt())
                .build();
    }
}
