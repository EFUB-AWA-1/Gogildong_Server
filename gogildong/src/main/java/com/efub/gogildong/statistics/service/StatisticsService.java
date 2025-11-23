package com.efub.gogildong.statistics.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.statistics.dto.FacilityStatsDto;
import com.efub.gogildong.statistics.dto.ReadRequestStatsDto;
import com.efub.gogildong.statistics.dto.ReportStatsDto;
import com.efub.gogildong.statistics.dto.SchoolStatsDto;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.repository.FacilityStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReportStatisticsRepository;
import com.efub.gogildong.statistics.repository.SchoolStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReadRequestStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

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
            case "school":
                return schoolStatisticsRepository.findStatistics(filter, pageable);

            case "facility":
                return facilityStatisticsRepository.findStatistics(filter, pageable);

            case "report":
                return reportStatisticsRepository.findStatistics(filter, pageable);

            case "readrequest":
                return readRequestStatisticsRepository.findStatistics(filter, pageable);

            default:
                throw new GoGildongException(ExceptionCode.ILLEGAL_ARGUMENT);
        }
    }

    public byte[] generateExcel(Map<String, StatisticsDataResponse<?>> dataMap) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        for (Map.Entry<String, StatisticsDataResponse<?>> entry : dataMap.entrySet()) {
            String sheetName = entry.getKey();
            StatisticsDataResponse<?> response = entry.getValue();

            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);

            // DTO별 헤더 작성
            Object firstItem = response.getItems().isEmpty() ? null : response.getItems().get(0);
            if (firstItem == null) continue;

            if (firstItem instanceof FacilityStatsDto) {
                headerRow.createCell(0).setCellValue("시설 ID");
                headerRow.createCell(1).setCellValue("시설 타입");
                headerRow.createCell(2).setCellValue("학교 이름");
                headerRow.createCell(3).setCellValue("건물 이름");
                headerRow.createCell(4).setCellValue("층 이름");
            } else if (firstItem instanceof ReportStatsDto) {
                headerRow.createCell(0).setCellValue("제보 ID");
                headerRow.createCell(1).setCellValue("학교 ID");
                headerRow.createCell(2).setCellValue("지역");
                headerRow.createCell(3).setCellValue("시설 타입");
                headerRow.createCell(4).setCellValue("상태");
                headerRow.createCell(5).setCellValue("공개 여부");
                headerRow.createCell(6).setCellValue("작성일");
            } else if (firstItem instanceof SchoolStatsDto) {
                headerRow.createCell(0).setCellValue("학교 ID");
                headerRow.createCell(1).setCellValue("학교 이름");
                headerRow.createCell(2).setCellValue("교육 단계");
                headerRow.createCell(3).setCellValue("지역");
                headerRow.createCell(4).setCellValue("특수학급 여부");
                headerRow.createCell(5).setCellValue("학생 수");
                headerRow.createCell(6).setCellValue("최근 활동일");
            } else if (firstItem instanceof ReadRequestStatsDto) {
                headerRow.createCell(0).setCellValue("요청 ID");
                headerRow.createCell(1).setCellValue("학교 ID");
                headerRow.createCell(2).setCellValue("지역");
                headerRow.createCell(3).setCellValue("상태");
                headerRow.createCell(4).setCellValue("사유");
                headerRow.createCell(5).setCellValue("생성일");
            }

            // 데이터 작성
            int rowIdx = 1;
            for (Object obj : response.getItems()) {
                Row row = sheet.createRow(rowIdx++);
                if (obj instanceof FacilityStatsDto f) {
                    row.createCell(0).setCellValue(f.getFacilityId());
                    row.createCell(1).setCellValue(f.getFacilityType().name());
                    row.createCell(2).setCellValue(f.getSchoolName());
                    row.createCell(3).setCellValue(f.getBuildingName());
                    row.createCell(4).setCellValue(f.getFloorName());
                } else if (obj instanceof ReportStatsDto r) {
                    row.createCell(0).setCellValue(r.getReportId());
                    row.createCell(1).setCellValue(r.getSchoolId());
                    row.createCell(2).setCellValue(r.getRegion());
                    row.createCell(3).setCellValue(r.getType().name());
                    row.createCell(4).setCellValue(r.getStatus().name());
                    row.createCell(5).setCellValue(r.getIsPublic());
                    row.createCell(6).setCellValue(r.getCreatedAt().toString());
                } else if (obj instanceof SchoolStatsDto s) {
                    row.createCell(0).setCellValue(s.getSchoolId());
                    row.createCell(1).setCellValue(s.getSchoolName());
                    row.createCell(2).setCellValue(s.getSchoolLevel().name());
                    row.createCell(3).setCellValue(s.getRegion());
                    row.createCell(4).setCellValue(s.getHasSpecialClass());
                    row.createCell(5).setCellValue(s.getStudentCount());
                    row.createCell(6).setCellValue(s.getLastActivityAt().toString());
                } else if (obj instanceof ReadRequestStatsDto rr) {
                    row.createCell(0).setCellValue(rr.getReadRequestId());
                    row.createCell(1).setCellValue(rr.getSchoolId());
                    row.createCell(2).setCellValue(rr.getRegion());
                    row.createCell(3).setCellValue(rr.getStatus().name());
                    row.createCell(4).setCellValue(rr.getReason());
                    row.createCell(5).setCellValue(rr.getCreatedAt().toString());
                }
            }
        }

        // 워크북 -> 바이트 배열 반환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }
}
