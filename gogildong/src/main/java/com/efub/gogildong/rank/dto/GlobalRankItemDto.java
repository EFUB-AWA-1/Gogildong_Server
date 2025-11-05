package com.efub.gogildong.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GlobalRankItemDto {
    private int rank;
    private Long user_id;
    private String user_name;
    private long total_score;
}
