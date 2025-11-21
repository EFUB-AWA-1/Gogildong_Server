package com.efub.gogildong.statistics.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadRequestStatisticsRepository {

//    private final JPAQueryFactory queryFactory;
//
//    public StatisticsResponse<ReadRequestStatsDto> findStatistics(
//            StatisticsFilterRequest filter,
//            Pageable pageable) {
//
//        QReadRequest read = QReadRequest.readRequest;
//
//        BooleanBuilder where = new BooleanBuilder();
//
//        // 공통
//        if (filter.getRegion() != null)
//            where.and(read.region.in(filter.getRegion()));
//
//        if (filter.getSchoolIds() != null)
//            where.and(read.schoolId.in(filter.getSchoolIds()));
//
//        if (filter.getFrom() != null)
//            where.and(read.createdAt.goe(LocalDateTime.parse(filter.getFrom())));
//
//        if (filter.getTo() != null)
//            where.and(read.createdAt.loe(LocalDateTime.parse(filter.getTo())));
//
//        // readRequest 전용
//        if (filter.getStatus() != null)
//            where.and(read.status.in(filter.getStatus()));
//
//        if (filter.getReason() != null)
//            where.and(read.reason.in(filter.getReason()));
//
//        List<ReadRequestStatsDto> items = queryFactory
//                .select(Projections.constructor(ReadRequestStatsDto.class,
//                        read.id,
//                        read.schoolId,
//                        read.region,
//                        read.status,
//                        read.reason,
//                        read.createdAt
//                ))
//                .from(read)
//                .where(where)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(QuerydslUtils.toOrderSpecifiers(pageable.getSort(), new PathBuilder<>(QReadRequest.class, "readRequest")))
//                .fetch();
//
//        long total = queryFactory
//                .select(read.count())
//                .from(read)
//                .where(where)
//                .fetchOne();
//
//        return StatisticsResponse.<ReadRequestStatsDto>builder()
//                .entity("readRequest")
//                .page(pageable.getPageNumber())
//                .size(pageable.getPageSize())
//                .totalElements(total)
//                .totalPages((int)Math.ceil((double)total / pageable.getPageSize()))
//                .items(items)
//                .exportToken(UUID.randomUUID().toString())
//                .build();
//    }
}
