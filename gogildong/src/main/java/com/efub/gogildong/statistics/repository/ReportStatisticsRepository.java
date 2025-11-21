package com.efub.gogildong.statistics.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportStatisticsRepository {

//    private final JPAQueryFactory queryFactory;
//
//    public StatisticsResponse<ReportStatsDto> findStatistics(
//            StatisticsFilterRequest filter,
//            Pageable pageable) {
//
//        QReport report = QReport.report;
//
//        BooleanBuilder where = new BooleanBuilder();
//
//        // 공통
//        if (filter.getRegion() != null)
//            where.and(report.region.in(filter.getRegion()));
//
//        if (filter.getSchoolIds() != null)
//            where.and(report.schoolId.in(filter.getSchoolIds()));
//
//        if (filter.getFrom() != null)
//            where.and(report.createdAt.goe(LocalDateTime.parse(filter.getFrom())));
//
//        if (filter.getTo() != null)
//            where.and(report.createdAt.loe(LocalDateTime.parse(filter.getTo())));
//
//        // report 전용
//        if (filter.getStatus() != null)
//            where.and(report.status.in(filter.getStatus()));
//
//        if (filter.getType() != null)
//            where.and(report.type.in(filter.getType()));
//
//        if (filter.getIsPublic() != null)
//            where.and(report.isPublic.eq(filter.getIsPublic()));
//
//        List<ReportStatsDto> items = queryFactory
//                .select(Projections.constructor(ReportStatsDto.class,
//                        report.id,
//                        report.schoolId,
//                        report.region,
//                        report.type,
//                        report.status,
//                        report.isPublic,
//                        report.createdAt
//                ))
//                .from(report)
//                .where(where)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(QuerydslUtils.toOrderSpecifiers(pageable.getSort(), new PathBuilder<>(QReport.class, "report")))
//                .fetch();
//
//        long total = queryFactory
//                .select(report.count())
//                .from(report)
//                .where(where)
//                .fetchOne();
//
//        return StatisticsResponse.<ReportStatsDto>builder()
//                .entity("report")
//                .page(pageable.getPageNumber())
//                .size(pageable.getPageSize())
//                .totalElements(total)
//                .totalPages((int)Math.ceil((double)total / pageable.getPageSize()))
//                .items(items)
//                .exportToken(UUID.randomUUID().toString())
//                .build();
//    }
}
