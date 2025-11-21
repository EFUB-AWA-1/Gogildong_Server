package com.efub.gogildong.statistics.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SchoolStatisticsRepository {

//    private final JPAQueryFactory queryFactory;
//
//    public StatisticsResponse<SchoolStatsDto> findStatistics(
//            StatisticsFilterRequest filter,
//            Pageable pageable) {
//
//        QSchool school = QSchool.school;
//
//        BooleanBuilder where = new BooleanBuilder();
//
//        // 공통
//        if (filter.getRegion() != null)
//            where.and(school.region.in(filter.getRegion()));
//
//        if (filter.getSchoolIds() != null)
//            where.and(school.id.in(filter.getSchoolIds()));
//
//        if (filter.getFrom() != null)
//            where.and(school.lastActivityAt.goe(LocalDateTime.parse(filter.getFrom())));
//
//        if (filter.getTo() != null)
//            where.and(school.lastActivityAt.loe(LocalDateTime.parse(filter.getTo())));
//
//        // school 전용
//        if (filter.getSchoolLevel() != null)
//            where.and(school.schoolLevel.eq(filter.getSchoolLevel()));
//
//        if (filter.getMinStudentCount() != null)
//            where.and(school.studentCount.goe(filter.getMinStudentCount()));
//
//        List<SchoolStatsDto> items = queryFactory
//                .select(Projections.constructor(SchoolStatsDto.class,
//                        school.id,
//                        school.name,
//                        school.schoolLevel,
//                        school.region,
//                        school.hasSpecialClass,
//                        school.studentCount,
//                        school.lastActivityAt
//                ))
//                .from(school)
//                .where(where)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(QuerydslUtils.toOrderSpecifiers(pageable.getSort(), new PathBuilder<>(QSchool.class, "school")))
//                .fetch();
//
//        long total = queryFactory
//                .select(school.count())
//                .from(school)
//                .where(where)
//                .fetchOne();
//
//        return StatisticsResponse.<SchoolStatsDto>builder()
//                .entity("school")
//                .page(pageable.getPageNumber())
//                .size(pageable.getPageSize())
//                .totalElements(total)
//                .totalPages((int)Math.ceil((double)total / pageable.getPageSize()))
//                .items(items)
//                .exportToken(UUID.randomUUID().toString())
//                .build();
//    }
}
