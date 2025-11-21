package com.efub.gogildong.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReadRequestStatsDto {

    private Long readRequestId;
    private Long schoolId;
    private String region;
    private String status;
    private String reason;
    private LocalDateTime createdAt;
}
