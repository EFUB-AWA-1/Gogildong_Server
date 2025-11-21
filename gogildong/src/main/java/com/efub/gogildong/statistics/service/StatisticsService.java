package com.efub.gogildong.statistics.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.repository.FacilityStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReportStatisticsRepository;
import com.efub.gogildong.statistics.repository.SchoolStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReadRequestStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final SchoolStatisticsRepository schoolStatisticsRepository;
    private final FacilityStatisticsRepository facilityStatisticsRepository;
    private final ReportStatisticsRepository reportStatisticsRepository;
    private final ReadRequestStatisticsRepository readRequestStatisticsRepository;


    public StatisticsDataResponse<?> getStatisticsData(
            String entity,
            StatisticsFilterRequest filter,
            Pageable pageable
    ) {
        switch (entity.toLowerCase()) {
//            case "school":
//                return schoolStatisticsRepository.findStatistics(filter, pageable);

            case "facility":
                return facilityStatisticsRepository.findStatistics(filter, pageable);

//            case "report":
//                return reportStatisticsRepository.findStatistics(filter, pageable);
//
//            case "readrequest":
//                return reportStatisticsRepository.findStatistics(filter, pageable);

            default:
                throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }
    }
}
