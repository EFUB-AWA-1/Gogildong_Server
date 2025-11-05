package com.efub.gogildong.rank.dto.response;

import com.efub.gogildong.rank.dto.SchoolRankItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SchoolRankListResponse {
    private List<SchoolRankItemDto> rankings;
}