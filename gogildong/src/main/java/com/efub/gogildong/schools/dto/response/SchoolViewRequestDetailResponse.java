package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.schools.domain.ReasonCategory;
import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchoolViewRequestDetailResponse {

    private final ReasonCategory reasonCategory;
    private final String requestReason;
    private final RequestStatus status;
    private final LocalDateTime createdAt;

    public static SchoolViewRequestDetailResponse from(SchoolViewRequest request) {
        return SchoolViewRequestDetailResponse.builder()
                .reasonCategory(request.getReasonCategory())
                .requestReason(request.getReason())
                .status(request.getStatus())
                .createdAt(request.getRequestedAt())
                .build();
    }
}
