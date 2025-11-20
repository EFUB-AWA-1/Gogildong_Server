package com.efub.gogildong.reports.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElevatorReportRequest extends NewElevatorReportRequest {
    private Long facilityId;
}