package com.efub.gogildong.reports.dto.response.classroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ClassroomAggregateStat {

    private Float avgDoorWidth;
    private Float avgDoorHandleHeight;
    private Float avgMinAisleWidth;
    private Float avgHasThreshold; // true 비율
    private Float minDoorWidth;
    private Float maxDoorWidth;
}
