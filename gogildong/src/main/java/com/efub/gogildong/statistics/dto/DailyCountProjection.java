package com.efub.gogildong.statistics.dto;

import java.time.LocalDate;

public interface DailyCountProjection {
    LocalDate getDate();
    long getCount();
}
