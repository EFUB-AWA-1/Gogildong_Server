package com.efub.gogildong.statistics.controller;

import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/data")
    public ResponseEntity<?> getStatisticsData(
            @RequestParam List<String> entity,
            @ModelAttribute StatisticsFilterRequest filterRequest,
            Pageable pageable
    ) {
        Map<String, StatisticsDataResponse<?>> result = new LinkedHashMap<>();

        for (String e : entity) {
            result.put(e.toLowerCase(), statisticsService.getStatisticsData(e, filterRequest, pageable));
        }

        return ResponseEntity.ok(result);
    }
}
