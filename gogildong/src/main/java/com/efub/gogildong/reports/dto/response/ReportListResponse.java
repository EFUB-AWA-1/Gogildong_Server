package com.efub.gogildong.reports.dto.response;

import com.efub.gogildong.reports.dto.summary.ReportSummary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReportListResponse {

    private List<ReportSummary> reports;

    public ReportListResponse(List<ReportSummary> reports) {
        this.reports = reports;
    }

}
