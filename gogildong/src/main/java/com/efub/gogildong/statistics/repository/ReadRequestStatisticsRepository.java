package com.efub.gogildong.statistics.repository;

import com.efub.gogildong.schools.domain.QSchoolViewRequest;
import com.efub.gogildong.statistics.dto.QReadRequestStatsDto;
import com.efub.gogildong.statistics.dto.ReadRequestStatsDto;
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
public class ReadRequestStatisticsRepository {

    private final JPAQueryFactory queryFactory;

    public StatisticsDataResponse<ReadRequestStatsDto> findStatistics(
            StatisticsFilterRequest filter,
            Pageable pageable) {

        QSchoolViewRequest read = QSchoolViewRequest.schoolViewRequest;

        BooleanBuilder where = new BooleanBuilder();

        // 공통 지역/날짜/학교ID 필터
        if (filter.getRegion() != null)
            where.and(read.school.region.in(filter.getRegion()));

        if (filter.getSchoolIds() != null)
            where.and(read.school.schoolId.in(filter.getSchoolIds()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (filter.getFrom() != null)
            where.and(read.requestedAt.goe(LocalDateTime.parse(filter.getFrom(), formatter)));

        if (filter.getTo() != null)
            where.and(read.requestedAt.loe(LocalDateTime.parse(filter.getTo(), formatter)));

        // readRequest 전용
        if (filter.getReadRequestStatus() != null)
            where.and(read.status.in(filter.getReadRequestStatus()));

        if (filter.getReason() != null)
            where.and(read.reason.in(filter.getReason()));

        // 페이징 + 정렬 + 조회
        List<ReadRequestStatsDto> items = queryFactory
                .select(new QReadRequestStatsDto(
                        read.requestId,
                        read.school.schoolId,
                        read.school.region,
                        read.status,
                        read.reason,
                        read.requestedAt
                ))
                .from(read)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(read.requestedAt.desc()) // 최신순 정렬
                .fetch();

        // total count
        Long total = queryFactory
                .select(read.count())
                .from(read)
                .where(where)
                .fetchOne();

        if (total == null)
            total = 0L;

        return StatisticsDataResponse.<ReadRequestStatsDto>builder()
                .entity("readRequest")
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / pageable.getPageSize()))
                .items(items)
                .exportToken(UUID.randomUUID().toString())
                .build();
    }
}
