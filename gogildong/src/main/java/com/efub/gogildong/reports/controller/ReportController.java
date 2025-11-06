package com.efub.gogildong.reports.controller;

import com.efub.gogildong.reports.dto.request.NewRestRoomReportRequest;
import com.efub.gogildong.reports.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /*
    * 새 화장실을 추가하고 해당 화장실에 대해 제보합니다.
    * */
    @PostMapping("/restroom")
    public ResponseEntity<Void> createReportAboutNewFacility(@RequestBody @Valid NewRestRoomReportRequest newFacilityReportRequest,
                                                             Authentication authentication) {
        reportService.createReportAboutNewFacility(authentication.getName(), newFacilityReportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
