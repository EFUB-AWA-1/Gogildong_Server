package com.efub.gogildong.statistics.dto;

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
    private String status;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}
