package com.efub.gogildong.schools.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SchoolViewRequestListResponse {

    private final List<SchoolViewRequestSummaryResponse> viewRequests;

    public static SchoolViewRequestListResponse of(List<SchoolViewRequestSummaryResponse> requests) {
        return SchoolViewRequestListResponse.builder()
                .viewRequests(requests)
                .build();
    }
}
