package com.efub.gogildong.rank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder @AllArgsConstructor
public class MyGlobalRankResponse {
    private Long user_id;
    private String user_name;
    private long score;
    private Integer global_rank;
    private Double global_percentile;
}
