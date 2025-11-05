package com.efub.gogildong.rank.dto.response;

import com.efub.gogildong.rank.dto.GlobalRankItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder @AllArgsConstructor
public class GlobalRankListResponse {
    private List<GlobalRankItemDto> rankings;
}
