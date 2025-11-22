package com.efub.gogildong.statistics.repository;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.schools.domain.QSchool;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.statistics.dto.ClassroomStatsDto;
import com.efub.gogildong.statistics.dto.ElevatorStatsDto;
import com.efub.gogildong.statistics.dto.FacilityStatsDto;
import com.efub.gogildong.statistics.dto.RestroomStatsDto;
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
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FacilityStatisticsRepository {

    private final JPAQueryFactory queryFactory;

    public StatisticsDataResponse<FacilityStatsDto> findStatistics(
            StatisticsFilterRequest filter,
            Pageable pageable
    ) {
        QFacility facility = QFacility.facility;
        QRestroom restroom = QRestroom.restroom;
        QFloor floor = QFloor.floor;
        QBuilding building = QBuilding.building;
        QSchool school = QSchool.school;

        // fetch join
        var query = queryFactory.selectFrom(facility)
                .leftJoin(facility.floor, floor).fetchJoin()
                .leftJoin(floor.building, building).fetchJoin()
                .leftJoin(building.school, school).fetchJoin()
                .leftJoin(restroom).on(restroom.facility.eq(facility)).fetchJoin();

        BooleanBuilder where = new BooleanBuilder();

        // 공통 지역/날짜/학교ID 필터
        if (filter.getRegion() != null) {
            where.and(school.isNotNull().and(school.region.in(filter.getRegion())));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (filter.getFrom() != null) {
            where.and(facility.updatedAt.goe(LocalDateTime.parse(filter.getFrom(), formatter)));
        }
        if (filter.getTo() != null) {
            where.and(facility.updatedAt.loe(LocalDateTime.parse(filter.getTo(), formatter)));
        }

        if (filter.getSchoolIds() != null) {
            where.and(school.isNotNull().and(school.schoolId.in(filter.getSchoolIds())));
        }

        // facility 필터
        if (filter.getFacilityType() != null) {
            where.and(facility.facilityType.in(filter.getFacilityType()));
        }

        if (filter.getBuildingId() != null) {
            where.and(building.isNotNull().and(building.buildingId.in(filter.getBuildingId())));
        }

        if (filter.getFloorId() != null) {
            where.and(floor.isNotNull().and(floor.floorId.in(filter.getFloorId())));
        }

        // restroom 전용 필터
        boolean isRestroom = filter.getFacilityType() != null &&
                filter.getFacilityType().contains(FacilityType.RESTROOM);

        if (isRestroom) {
            if (filter.getDoorType() != null)
                where.and(restroom.doorType.in(filter.getDoorType()));
            if (filter.getDoorWidthMin() != null)
                where.and(restroom.doorWidth.goe(filter.getDoorWidthMin()));
            if (filter.getDoorHeightMin() != null)
                where.and(restroom.doorHeight.goe(filter.getDoorHeightMin()));
        }

        // where 적용 후 fetch
        List<Facility> facilities = query.where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // DTO 변환 (null-safe)
        List<FacilityStatsDto> items = facilities.stream().map(f -> {
            Floor fl = f.getFloor();
            Building b = (fl != null) ? fl.getBuilding() : null;
            School s = (b != null) ? b.getSchool() : null;

            String schoolName = (s != null) ? s.getSchoolName() : null;
            String region = (s != null) ? s.getRegion() : null;
            String buildingName = (b != null) ? b.getBuildingName() : null;
            String floorName = (fl != null) ? fl.getFloorName() : null;

            if (f.getFacilityType() == FacilityType.RESTROOM) {
                Restroom r = f.getRestroom();
                return RestroomStatsDto.builder()
                        .facilityId(f.getFacilityId())
                        .facilityType(f.getFacilityType())
                        .schoolId(s != null ? s.getSchoolId() : null)
                        .schoolName(schoolName)
                        .buildingName(buildingName)
                        .floorName(floorName)
                        .region(region)
                        .lastActivityAt(f.getUpdatedAt())

                        .doorType(r != null ? r.getDoorType() : null)
                        .doorWidth(r != null ? r.getDoorWidth() : null)
                        .doorHeight(r != null ? r.getDoorHeight() : null)
                        .grabBar(r != null ? r.getGrabBar() : null)
                        .isAccessible(r != null ? r.getIsAccessible() : null)

                        .build();

            } else {
                return FacilityStatsDto.builder()
                        .facilityId(f.getFacilityId())
                        .facilityType(f.getFacilityType())
                        .schoolId(s != null ? s.getSchoolId() : null)
                        .schoolName(schoolName)
                        .buildingName(buildingName)
                        .floorName(floorName)
                        .region(region)
                        .lastActivityAt(f.getUpdatedAt())
                        .build();
            }
        }).collect(Collectors.toList());

        // total count
        long total = queryFactory.select(facility.count())
                .from(facility)
                .leftJoin(facility.floor, floor)
                .leftJoin(floor.building, building)
                .leftJoin(building.school, school)
                .where(where)
                .fetchOne();

        return StatisticsDataResponse.<FacilityStatsDto>builder()
                .entity("facility")
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(total)
                .totalPages((int) Math.ceil((double) total / pageable.getPageSize()))
                .items(items)
                .exportToken(UUID.randomUUID().toString())
                .build();
    }
}