package com.efub.gogildong.reports.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestRoomReportRequest extends NewRestRoomReportRequest {
    private Long facilityId;
}
