package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SchoolListResponse {
    private long totalElements;
    private int totalPages;
    private boolean last;
    private List<SchoolSummaryResponse> schools;
}
