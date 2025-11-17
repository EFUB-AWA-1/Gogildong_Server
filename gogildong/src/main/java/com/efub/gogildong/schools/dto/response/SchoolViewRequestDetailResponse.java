package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.schools.domain.ReasonCategory;
import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.user.dto.response.RequesterSummaryResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchoolViewRequestDetailResponse {

    private final Long requestId;
    private final RequesterSummaryResponse requester;
    private final String schoolName;
    private final ReasonCategory reasonCategory;
    private final String requestReason;
    private final RequestStatus status;

    public static SchoolViewRequestDetailResponse from(SchoolViewRequest request) {
        return SchoolViewRequestDetailResponse.builder()
                .requestId(request.getRequestId())
                .requester(RequesterSummaryResponse.from(request.getUser()))
                .schoolName(request.getSchool().getSchoolName())
                .reasonCategory(request.getReasonCategory())
                .requestReason(request.getReason())
                .status(request.getStatus())
                .build();
    }
}
