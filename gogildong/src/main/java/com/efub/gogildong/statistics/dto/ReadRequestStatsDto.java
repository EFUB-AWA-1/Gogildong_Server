package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.schools.domain.RequestStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReadRequestStatsDto {

    private Long readRequestId;
    private Long schoolId;
    private String region;
    private RequestStatus status;
    private String reason;
    private LocalDateTime createdAt;

    @QueryProjection
    public ReadRequestStatsDto(Long readRequestId,
                               Long schoolId,
                               String region,
                               RequestStatus status,
                               String reason,
                               LocalDateTime createdAt) {
        this.readRequestId = readRequestId;
        this.schoolId = schoolId;
        this.region = region;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
    }
}
