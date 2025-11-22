package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.reports.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReportStatsDto {

    private Long reportId;
    private Long schoolId;
    private String region;
    private String type;
    private ReportStatus status;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}
