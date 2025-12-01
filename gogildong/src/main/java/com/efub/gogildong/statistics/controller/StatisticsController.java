package com.efub.gogildong.statistics.controller;

import com.efub.gogildong.global.util.CsvUtils;
import com.efub.gogildong.statistics.dto.FacilityStatsDto;
import com.efub.gogildong.statistics.dto.ReadRequestStatsDto;
import com.efub.gogildong.statistics.dto.ReportStatsDto;
import com.efub.gogildong.statistics.dto.SchoolStatsDto;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.service.StatisticsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

//    @GetMapping("/data/export")
//    public ResponseEntity<byte[]> downloadExcel(@RequestParam List<String> entity,
//                                                @ModelAttribute StatisticsFilterRequest filterRequest,
//                                                Pageable pageable) throws IOException {
//        // 1. 데이터 조회
//        Map<String, StatisticsDataResponse<?>> result = new LinkedHashMap<>();
//        for (String e : entity) {
//            result.put(e.toLowerCase(), statisticsService.getStatisticsData(e, filterRequest, pageable));
//        }
//
//        // 2. 워크북 생성
//        Workbook workbook = new XSSFWorkbook();
//
//        for (Map.Entry<String, StatisticsDataResponse<?>> entry : result.entrySet()) {
//            String sheetName = entry.getKey();
//            StatisticsDataResponse<?> response = entry.getValue();
//
//            Sheet sheet = workbook.createSheet(sheetName);
//            Row headerRow = sheet.createRow(0);
//
//            // 3. DTO별 헤더 설정
//            Object firstItem = response.getItems().isEmpty() ? null : response.getItems().get(0);
//            if (firstItem == null) continue;
//
//            if (firstItem instanceof FacilityStatsDto) {
//                headerRow.createCell(0).setCellValue("시설 ID");
//                headerRow.createCell(1).setCellValue("시설 타입");
//                headerRow.createCell(2).setCellValue("학교 이름");
//                headerRow.createCell(3).setCellValue("건물 이름");
//                headerRow.createCell(4).setCellValue("층 이름");
//            } else if (firstItem instanceof ReportStatsDto) {
//                headerRow.createCell(0).setCellValue("제보 ID");
//                headerRow.createCell(1).setCellValue("학교 ID");
//                headerRow.createCell(2).setCellValue("지역");
//                headerRow.createCell(3).setCellValue("시설 타입");
//                headerRow.createCell(4).setCellValue("상태");
//                headerRow.createCell(5).setCellValue("공개 여부");
//                headerRow.createCell(6).setCellValue("작성일");
//            } else if (firstItem instanceof SchoolStatsDto) {
//                headerRow.createCell(0).setCellValue("학교 ID");
//                headerRow.createCell(1).setCellValue("학교 이름");
//                headerRow.createCell(2).setCellValue("교육 단계");
//                headerRow.createCell(3).setCellValue("지역");
//                headerRow.createCell(4).setCellValue("특수학급 여부");
//                headerRow.createCell(5).setCellValue("학생 수");
//                headerRow.createCell(6).setCellValue("최근 활동일");
//            } else if (firstItem instanceof ReadRequestStatsDto) {
//                headerRow.createCell(0).setCellValue("요청 ID");
//                headerRow.createCell(1).setCellValue("학교 ID");
//                headerRow.createCell(2).setCellValue("지역");
//                headerRow.createCell(3).setCellValue("상태");
//                headerRow.createCell(4).setCellValue("사유");
//                headerRow.createCell(5).setCellValue("생성일");
//            }
//
//            // 4. 데이터 작성
//            int rowIdx = 1;
//            for (Object obj : response.getItems()) {
//                Row row = sheet.createRow(rowIdx++);
//                if (obj instanceof FacilityStatsDto f) {
//                    row.createCell(0).setCellValue(f.getFacilityId());
//                    row.createCell(1).setCellValue(f.getFacilityType().name());
//                    row.createCell(2).setCellValue(f.getSchoolName());
//                    row.createCell(3).setCellValue(f.getBuildingName());
//                    row.createCell(4).setCellValue(f.getFloorName());
//                } else if (obj instanceof ReportStatsDto r) {
//                    row.createCell(0).setCellValue(r.getReportId());
//                    row.createCell(1).setCellValue(r.getSchoolId());
//                    row.createCell(2).setCellValue(r.getRegion());
//                    row.createCell(3).setCellValue(r.getType().name());
//                    row.createCell(4).setCellValue(r.getStatus().name());
//                    row.createCell(5).setCellValue(r.getIsPublic());
//                    row.createCell(6).setCellValue(r.getCreatedAt().toString());
//                } else if (obj instanceof SchoolStatsDto s) {
//                    row.createCell(0).setCellValue(s.getSchoolId());
//                    row.createCell(1).setCellValue(s.getSchoolName());
//                    row.createCell(2).setCellValue(s.getSchoolLevel().name());
//                    row.createCell(3).setCellValue(s.getRegion());
//                    row.createCell(4).setCellValue(s.getHasSpecialClass());
//                    row.createCell(5).setCellValue(s.getStudentCount());
//                    row.createCell(6).setCellValue(s.getLastActivityAt().toString());
//                } else if (obj instanceof ReadRequestStatsDto rr) {
//                    row.createCell(0).setCellValue(rr.getReadRequestId());
//                    row.createCell(1).setCellValue(rr.getSchoolId());
//                    row.createCell(2).setCellValue(rr.getRegion());
//                    row.createCell(3).setCellValue(rr.getStatus().name());
//                    row.createCell(4).setCellValue(rr.getReason());
//                    row.createCell(5).setCellValue(rr.getCreatedAt().toString());
//                }
//            }
//        }
//
//        // 5. 워크북 -> 바이트 배열
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        workbook.write(baos);
//        workbook.close();
//
//        // 6. ResponseEntity 반환
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(baos.toByteArray());
//    }

    @GetMapping("/data/export")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam List<String> entity,
                                                @ModelAttribute StatisticsFilterRequest filterRequest,
                                                Pageable pageable) throws IOException {
        // 데이터 조회
        Map<String, StatisticsDataResponse<?>> result = new LinkedHashMap<>();
        for (String e : entity) {
            result.put(e.toLowerCase(), statisticsService.getStatisticsData(e, filterRequest, pageable));
        }

        // Excel 생성
        byte[] excelBytes = statisticsService.generateExcel(result);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(statisticsService.getDashboard(year, month));
    }

}
