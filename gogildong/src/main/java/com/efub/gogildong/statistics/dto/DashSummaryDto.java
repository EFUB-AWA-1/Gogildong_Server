package com.efub.gogildong.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashSummaryDto {
    private long current;
    private long previous;
    private long diff;
    private String rate;
}
