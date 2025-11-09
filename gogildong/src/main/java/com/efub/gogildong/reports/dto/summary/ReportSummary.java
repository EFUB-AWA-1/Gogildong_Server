package com.efub.gogildong.reports.dto.summary;

import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportSummary {

    private Long reportId;
    private int flagCount;
    private Boolean isPublic;
    private String loginId;

    public static ReportSummary from(Report report) {
        if (report == null) return null;
        return ReportSummary.builder()
                .reportId(report.getReportId())
                .flagCount(report.getFlagCount())
                .isPublic(report.getIsPublic())
                .loginId(report.getUser().getLoginId())
                .build();
    }
}
