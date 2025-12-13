package com.efub.gogildong.reports.dto.response.elevator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ElevatorAggregateStat {
    private Float avgDoorWidth;
    private Float avgInteriorDepth;
    private Float avgMaxControlPanelHeight;
    private Float minDoorWidth;
    private Float maxDoorWidth;
}
