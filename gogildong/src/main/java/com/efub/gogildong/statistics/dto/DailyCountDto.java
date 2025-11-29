package com.efub.gogildong.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyCountDto {
    private LocalDate date;
    private long count;
}
