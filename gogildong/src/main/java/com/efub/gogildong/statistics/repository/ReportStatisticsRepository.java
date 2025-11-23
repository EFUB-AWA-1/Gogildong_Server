package com.efub.gogildong.statistics.repository;

import com.efub.gogildong.facility.domain.QBuilding;
import com.efub.gogildong.facility.domain.QFacility;
import com.efub.gogildong.facility.domain.QFloor;
import com.efub.gogildong.reports.domain.QReport;
import com.efub.gogildong.statistics.dto.QReportStatsDto;
import com.efub.gogildong.statistics.dto.ReportStatsDto;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReportStatisticsRepository {

    private final JPAQueryFactory queryFactory;

    public StatisticsDataResponse<ReportStatsDto> findStatistics(
            StatisticsFilterRequest filter,
            Pageable pageable) {

        QReport report = QReport.report;
        QFacility facility = QFacility.facility;
        QFloor floor = QFloor.floor;
        QBuilding building = QBuilding.building;

        BooleanBuilder where = new BooleanBuilder();

        // 공통
        if (filter.getRegion() != null)
            where.and(building.school.region.in(filter.getRegion()));

        if (filter.getSchoolIds() != null)
            where.and(building.school.schoolId.in(filter.getSchoolIds()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (filter.getFrom() != null)
            where.and(report.createdAt.goe(LocalDateTime.parse(filter.getFrom(), formatter)));

        if (filter.getTo() != null)
            where.and(report.createdAt.loe(LocalDateTime.parse(filter.getTo(), formatter)));

        // report 전용
        if (filter.getReportStatus() != null)
            where.and(report.status.in(filter.getReportStatus()));

        if (filter.getReportType() != null)
            where.and(report.reportType.in(filter.getReportType()));

        if (filter.getIsPublic() != null)
            where.and(report.isPublic.eq(filter.getIsPublic()));

        // 페이징 + 정렬 + 조회

        List<ReportStatsDto> items = queryFactory
                .select(new QReportStatsDto(
                        report.reportId,
                        building.school.schoolId,
                        building.school.region,
                        report.reportType,
                        report.status,
                        report.isPublic,
                        report.createdAt
                ))
                .from(report)
                .leftJoin(report.facility, facility)
                .leftJoin(facility.floor, floor)
                .leftJoin(floor.building, building)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(report.createdAt.desc())
                .fetch();

        // total count
        Long total = queryFactory
                .select(report.count())
                .from(report)
                .where(where)
                .fetchOne();

        if (total == null)
            total = 0L;

        return StatisticsDataResponse.<ReportStatsDto>builder()
                .entity("report")
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(total)
                .totalPages((int)Math.ceil((double)total / pageable.getPageSize()))
                .items(items)
                .exportToken(UUID.randomUUID().toString())
                .build();
    }
}
