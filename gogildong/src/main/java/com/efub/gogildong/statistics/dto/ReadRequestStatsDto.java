package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.schools.domain.RequestStatus;
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
    private RequestStatus status;
    private String reason;
    private LocalDateTime createdAt;
}
