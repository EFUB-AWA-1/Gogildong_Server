package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.ReportStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportStatsDto {

    private Long reportId;
    private Long schoolId;
    private String region;
    private FacilityType type;
    private ReportStatus status;
    private Boolean isPublic;
    private LocalDateTime createdAt;

    @QueryProjection
    public ReportStatsDto(Long reportId, Long schoolId, String region,
                          FacilityType type, ReportStatus status,
                          Boolean isPublic, LocalDateTime createdAt) {
        this.reportId = reportId;
        this.schoolId = schoolId;
        this.region = region;
        this.type = type;
        this.status = status;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }
}
