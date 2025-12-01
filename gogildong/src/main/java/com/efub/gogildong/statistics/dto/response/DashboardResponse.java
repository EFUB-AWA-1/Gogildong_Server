package com.efub.gogildong.statistics.dto.response;

import com.efub.gogildong.statistics.dto.DailyCountDto;
import com.efub.gogildong.statistics.dto.DashSummaryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DashboardResponse {

    private int year;
    private int month;

    private DashSummaryDto reports;
    private DashSummaryDto viewRequests;
    private DashSummaryDto newUsers;
    private DashSummaryDto participatingSchools;

    // 장소별 제보 요약
    private Map<String, DashSummaryDto> placeSummary;

    private DailySection daily;

    @Getter
    @Builder
    public static class DailySection {
        private List<DailyCountDto> reports;
        private List<DailyCountDto> viewRequests;
        private List<DailyCountDto> newUsers;
        private List<DailyCountDto> participatingSchools;
    }
}
