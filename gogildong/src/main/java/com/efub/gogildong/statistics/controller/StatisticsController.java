package com.efub.gogildong.statistics.controller;

import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/data")
    public ResponseEntity<StatisticsDataResponse<?>> getStatisticsData(
            @RequestParam String entity,
            @ModelAttribute StatisticsFilterRequest filterRequest,
            Pageable pageable
    ) {
        StatisticsDataResponse<?> response =
                statisticsService.getStatisticsData(entity, filterRequest, pageable);

        return ResponseEntity.ok(response);
    }
}
