package com.efub.gogildong.rank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MySchoolVsSchoolResponse {
    private Long user_id;
    private Long school_id;
    private String school_name;
    private long score;
    private Integer school_rank;
    private Double school_percentile;
}
