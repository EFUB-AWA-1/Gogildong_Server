package com.efub.gogildong.rank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SchoolRankItemDto {
    private int rank;
    private Long school_id;
    private String school_name;
    private long total_score;      // 학교 총합 점수
}
