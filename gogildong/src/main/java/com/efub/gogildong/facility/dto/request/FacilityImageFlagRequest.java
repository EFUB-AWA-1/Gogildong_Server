package com.efub.gogildong.facility.dto.request;

import com.efub.gogildong.reports.domain.ReportFlag;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityImageFlagRequest {
    private Long reportId;
    private String reason;

    public ReportFlag toEntity(Report report, User user) {
        return ReportFlag.builder()
                .report(report)
                .user(user)
                .reason(this.reason)
                .build();
    }
}
