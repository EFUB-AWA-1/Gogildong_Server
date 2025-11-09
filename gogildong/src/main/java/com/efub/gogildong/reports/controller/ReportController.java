package com.efub.gogildong.reports.controller;

import com.efub.gogildong.reports.dto.request.NewRestRoomReportRequest;
import com.efub.gogildong.reports.dto.request.RestRoomReportRequest;
import com.efub.gogildong.reports.dto.response.ReportListResponse;
import com.efub.gogildong.reports.dto.response.RestRoomReportResponse;
import com.efub.gogildong.reports.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /*
    * 새 화장실을 추가하고 해당 화장실에 대해 제보합니다.
    * */
    @PostMapping("/restroom/new-facility")
    public ResponseEntity<Void> createReportAboutNewRestroom(@RequestBody @Valid NewRestRoomReportRequest newFacilityReportRequest,
                                                             Authentication authentication) {
        reportService.createReportAboutNewRestroom(authentication.getName(), newFacilityReportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    * 기존 화장실에 대해 제보합니다.
    * */
    @PostMapping("/restroom")
    public ResponseEntity<Void> createReportAboutExistingRestroom(@RequestBody @Valid RestRoomReportRequest restRoomReportRequest,
                                                                  Authentication authentication) {
        reportService.createReportAboutExistingRestroom(authentication.getName(), restRoomReportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    화장실 제보를 상세 조회합니다. (학교 관리자)
     */
    @GetMapping("/restroom/{restRoomReportId}")
    public ResponseEntity<RestRoomReportResponse> getRestRoomReport(@PathVariable("restRoomReportId") Long restRoomReportId,
                                                                    Authentication authentication) {
        reportService.getRestRoomReport(restRoomReportId);
        return ResponseEntity.ok(reportService.getRestRoomReport(restRoomReportId));
    }

    /*
    제보 전체 목록을 조회합니다. (학교 관리자)
     */
    @GetMapping(" ")
    public ResponseEntity<ReportListResponse> getAllReport() {
        return ResponseEntity.ok(reportService.getAllReports());
    }


}
