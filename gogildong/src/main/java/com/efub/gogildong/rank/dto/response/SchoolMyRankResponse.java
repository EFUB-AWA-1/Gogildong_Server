package com.efub.gogildong.rank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SchoolMyRankResponse {
    private Long user_id;
    private String user_name;
    private long score;
    private Integer my_rank;
    private Double my_percentile;
}