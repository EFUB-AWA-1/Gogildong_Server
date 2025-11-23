package com.efub.gogildong.statistics.repository;

import com.efub.gogildong.schools.domain.QSchool;
import com.efub.gogildong.statistics.dto.QSchoolStatsDto;
import com.efub.gogildong.statistics.dto.SchoolStatsDto;
import com.efub.gogildong.statistics.dto.request.StatisticsFilterRequest;
import com.efub.gogildong.statistics.dto.response.StatisticsDataResponse;
import com.efub.gogildong.user.domain.QUser;
import com.efub.gogildong.user.domain.UserRole;
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
public class SchoolStatisticsRepository {

    private final JPAQueryFactory queryFactory;

    public StatisticsDataResponse<SchoolStatsDto> findStatistics(
            StatisticsFilterRequest filter,
            Pageable pageable) {

        QSchool school = QSchool.school;
        QUser user = QUser.user;

        BooleanBuilder where = new BooleanBuilder();

        // 공통
        if (filter.getRegion() != null)
            where.and(school.region.in(filter.getRegion()));

        if (filter.getSchoolIds() != null)
            where.and(school.schoolId.in(filter.getSchoolIds()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (filter.getFrom() != null)
            where.and(school.updatedAt.goe(LocalDateTime.parse(filter.getFrom(), formatter)));

        if (filter.getTo() != null)
            where.and(school.updatedAt.loe(LocalDateTime.parse(filter.getTo(), formatter)));

        // school 전용
        if (filter.getSchoolLevel() != null)
            where.and(school.eduLevel.eq(filter.getSchoolLevel()));

        // 학생 수 조건 (최소 학생 수)
        // 나중에 filter.getMinStudentCount() 적용 가능
        BooleanBuilder havingStudentCount = new BooleanBuilder();
        if (filter.getMinStudentCount() != null) {
            havingStudentCount.and(
                    queryFactory.select(user.count())
                            .from(user)
                            .where(user.school.eq(school)
                                    .and(user.role.eq(UserRole.INTERNAL)))
                            .goe(Long.valueOf(filter.getMinStudentCount()))
            );
        }

        // 페이징 + 정렬 + 조회
        List<SchoolStatsDto> items = queryFactory
                .select(new QSchoolStatsDto(
                        school.schoolId,
                        school.schoolName,
                        school.eduLevel,
                        school.region,
                        school.hasSpecialClass,
                        queryFactory.select(user.count().intValue())
                                .from(user)
                                .where(user.school.eq(school)
                                        .and(user.role.eq(UserRole.INTERNAL))),
                        school.updatedAt
                ))
                .from(school)
                .where(where)
                .orderBy(school.schoolId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 최소 학생 수 필터 적용
        if (filter.getMinStudentCount() != null) {
            items.removeIf(dto -> dto.getStudentCount() < filter.getMinStudentCount());
        }

        Long total = queryFactory
                .select(school.count())
                .from(school)
                .where(where)
                .fetchOne();

        if (total == null)
            total = 0L;

        return StatisticsDataResponse.<SchoolStatsDto>builder()
                .entity("school")
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(total)
                .totalPages((int)Math.ceil((double)total / pageable.getPageSize()))
                .items(items)
                .exportToken(UUID.randomUUID().toString())
                .build();
    }
}
