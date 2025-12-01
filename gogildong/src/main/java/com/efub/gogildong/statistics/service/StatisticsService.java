package com.efub.gogildong.statistics.service;

import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.reports.repository.ReportRepository;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.schools.repository.SchoolViewRequestRepository;
import com.efub.gogildong.statistics.dto.*;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.DashboardResponse;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.statistics.repository.FacilityStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReportStatisticsRepository;
import com.efub.gogildong.statistics.repository.SchoolStatisticsRepository;
import com.efub.gogildong.statistics.repository.ReadRequestStatisticsRepository;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final SchoolStatisticsRepository schoolStatisticsRepository;
    private final FacilityStatisticsRepository facilityStatisticsRepository;
    private final ReportStatisticsRepository reportStatisticsRepository;
    private final ReadRequestStatisticsRepository readRequestStatisticsRepository;

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolViewRequestRepository schoolViewRequestRepository;   // 열람 요청

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

    public DashboardResponse getDashboard(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        YearMonth prevYm = ym.minusMonths(1);
        LocalDate prevStart = prevYm.atDay(1);
        LocalDate prevEnd = prevYm.atEndOfMonth();

        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.plusDays(1).atStartOfDay();
        LocalDateTime prevStartDt = prevStart.atStartOfDay();
        LocalDateTime prevEndDt = prevEnd.plusDays(1).atStartOfDay();

        // --- 1) 월 요약 섹션 ---

        // 제보 수
        DashSummaryDto reportsSummary = buildDashSummary(
                () -> reportRepository.countByCreatedAtBetween(startDt, endDt),
                () -> reportRepository.countByCreatedAtBetween(prevStartDt, prevEndDt)
        );

        // 열람 요청 수
        DashSummaryDto viewRequestsSummary = buildDashSummary(
                () -> schoolViewRequestRepository.countByRequestedAtBetween(startDt, endDt),
                () -> schoolViewRequestRepository.countByRequestedAtBetween(prevStartDt, prevEndDt)
        );

        // 신규 사용자 수
        DashSummaryDto newUsersSummary = buildDashSummary(
                () -> userRepository.countByCreatedAtBetween(startDt, endDt),
                () -> userRepository.countByCreatedAtBetween(prevStartDt, prevEndDt)
        );

        // 참여 학교 수 (누적)
        DashSummaryDto schoolsSummary = buildParticipatingSchoolsSummary(end, prevEnd);

        // 장소별 제보 (RESTROOM / ELEVATOR / CLASSROOM)
        Map<String, DashSummaryDto> placeSummary =
                buildPlaceSummary(startDt, endDt, prevStartDt, prevEndDt);

        // --- 2) 일별 섹션 ---

        // 제보 일별
        List<DailyCountDto> dailyReports = fillDailyCounts(
                start, end,
                reportRepository.countDaily(startDt, endDt)
        );

        // 열람 요청 일별
        List<DailyCountDto> dailyViewRequests = fillDailyCounts(
                start, end,
                schoolViewRequestRepository.countDaily(startDt, endDt)
        );

        // 신규 사용자 일별
        List<DailyCountDto> dailyNewUsers = fillDailyCounts(
                start, end,
                userRepository.countDaily(startDt, endDt)
        );

        // 참여 학교 일별(누적)
        List<DailyCountDto> dailySchools =
                buildDailyParticipatingSchools(start, end);

        return DashboardResponse.builder()
                .year(year)
                .month(month)
                .reports(reportsSummary)
                .viewRequests(viewRequestsSummary)
                .newUsers(newUsersSummary)
                .participatingSchools(schoolsSummary)
                .placeSummary(placeSummary)
                .daily(DashboardResponse.DailySection.builder()
                        .reports(dailyReports)
                        .viewRequests(dailyViewRequests)
                        .newUsers(dailyNewUsers)
                        .participatingSchools(dailySchools)
                        .build())
                .build();
    }

    private DashSummaryDto buildDashSummary(LongSupplier currentSupplier, LongSupplier previousSupplier) {
        long current = currentSupplier.getAsLong();
        long previous = previousSupplier.getAsLong();

        return DashSummaryDto.builder()
                .current(current)
                .previous(previous)
                .diff(current - previous)
                .rate(calculateRate(current, previous))
                .build();
    }


    private String calculateRate(long current, long previous) {
        if (previous == 0) {
            if (current == 0) return "0%";
            return "-%";
        }

        double value = ((double) (current - previous) / previous) * 100.0;
        return String.format("%.1f%%", value);  // 예: "25.3%"
    }


    // 참여 학교 월 요약 (누적)
    private DashSummaryDto buildParticipatingSchoolsSummary(LocalDate end, LocalDate prevEnd) {
        long previous = schoolRepository.countByCreatedAtBefore(prevEnd.plusDays(1).atStartOfDay());
        long current = schoolRepository.countByCreatedAtBefore(end.plusDays(1).atStartOfDay());

        return DashSummaryDto.builder()
                .current(current)
                .previous(previous)
                .diff(current - previous)
                .rate(calculateRate(current, previous))
                .build();
    }

    // 장소별 제보 요약 (RESTROOM / ELEVATOR / CLASSROOM)
    private Map<String, DashSummaryDto> buildPlaceSummary(
            LocalDateTime startDt, LocalDateTime endDt,
            LocalDateTime prevStartDt, LocalDateTime prevEndDt
    ) {
        Map<FacilityType, Long> currentMap = reportRepository.countByReportTypeBetween(startDt, endDt)
                .stream()
                .collect(Collectors.toMap(
                        ReportRepository.FacilityTypeCountProjection::getReportType,
                        ReportRepository.FacilityTypeCountProjection::getCount
                ));

        Map<FacilityType, Long> prevMap = reportRepository.countByReportTypeBetween(prevStartDt, prevEndDt)
                .stream()
                .collect(Collectors.toMap(
                        ReportRepository.FacilityTypeCountProjection::getReportType,
                        ReportRepository.FacilityTypeCountProjection::getCount
                ));

        Map<String, DashSummaryDto> result = new HashMap<>();

        for (FacilityType type : FacilityType.values()) {
            long curr = currentMap.getOrDefault(type, 0L);
            long prev = prevMap.getOrDefault(type, 0L);

            result.put(type.name(), DashSummaryDto.builder()
                    .current(curr)
                    .previous(prev)
                    .diff(curr - prev)
                    .rate(calculateRate(curr, prev))
                    .build());
        }

        return result;
    }

    // 공통: raw 일별 결과를 start~end 전체 날짜 리스트로 채우기
    private List<DailyCountDto> fillDailyCounts(
            LocalDate start, LocalDate end,
            List<DailyCountProjection> raw
    ) {
        Map<LocalDate, Long> map = raw.stream()
                .collect(Collectors.toMap(DailyCountProjection::getDate, DailyCountProjection::getCount));

        List<DailyCountDto> result = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            result.add(new DailyCountDto(d, map.getOrDefault(d, 0L)));
        }
        return result;
    }

    // 참여 학교: 일별 누적 시리즈 만들기
    private List<DailyCountDto> buildDailyParticipatingSchools(LocalDate start, LocalDate end) {
        long base = schoolRepository.countByCreatedAtBefore(start.atStartOfDay());

        List<DailyCountProjection> raw =
                schoolRepository.countDailyNewSchools(start.atStartOfDay(), end.plusDays(1).atStartOfDay());

        Map<LocalDate, Long> newMap = raw.stream()
                .collect(Collectors.toMap(DailyCountProjection::getDate, DailyCountProjection::getCount));

        List<DailyCountDto> result = new ArrayList<>();
        long cumulative = base;

        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            cumulative += newMap.getOrDefault(d, 0L);
            result.add(new DailyCountDto(d, cumulative));
        }
        return result;
    }
}
