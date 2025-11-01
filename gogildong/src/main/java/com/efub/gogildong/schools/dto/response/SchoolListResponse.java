package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SchoolListResponse {
    private int total;
    private List<SchoolSummaryResponse> schools;
}
