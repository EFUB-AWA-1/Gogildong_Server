package com.efub.gogildong.reports.dto.response;

import com.efub.gogildong.reports.dto.summary.ReportFlagSummary;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReportFlagListResponse {

    private Long reportId;
    private List<ReportFlagSummary> flags;

    public static ReportFlagListResponse of(Long reportId, List<ReportFlagSummary> flags) {
        return ReportFlagListResponse.builder()
                .reportId(reportId)
                .flags(flags)
                .build();
    }
}
