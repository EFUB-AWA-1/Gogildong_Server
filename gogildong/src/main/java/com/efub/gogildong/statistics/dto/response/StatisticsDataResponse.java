package com.efub.gogildong.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticsDataResponse<T> {

    private String entity;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    private List<T> items;

    private String exportToken;
}
