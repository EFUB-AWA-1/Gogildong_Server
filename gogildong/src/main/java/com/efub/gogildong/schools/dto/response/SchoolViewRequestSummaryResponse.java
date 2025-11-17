package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchoolViewRequestSummaryResponse {

    private final Long requestId;
    private final String loginId;
    private final String username;
    private final String email;
    private final Long schoolId;
    private final String schoolName;
    private final RequestStatus status;
    private final LocalDateTime createdAt;

    public static SchoolViewRequestSummaryResponse from(SchoolViewRequest request) {
        return SchoolViewRequestSummaryResponse.builder()
                .requestId(request.getRequestId())
                .loginId(request.getUser().getLoginId())
                .username(request.getUser().getUsername())
                .email(request.getUser().getEmail())
                .schoolId(request.getSchool().getSchoolId())
                .schoolName(request.getSchool().getSchoolName())
                .status(request.getStatus())
                .createdAt(request.getRequestedAt())
                .build();
    }
}
