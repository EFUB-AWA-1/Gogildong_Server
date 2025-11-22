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
        QFloor floor = QFloor.floor;
        QBuilding building = QBuilding.building;
        QSchool school = QSchool.school;

        // fetch join
        var query = queryFactory.selectFrom(facility)
                .leftJoin(facility.floor, floor).fetchJoin()
                .leftJoin(floor.building, building).fetchJoin()
                .leftJoin(building.school, school).fetchJoin();

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
            QRestroom restroom = QRestroom.restroom;
            query.leftJoin(restroom).on(restroom.facility.eq(facility)).fetchJoin();

            if (filter.getRestroomDoorType() != null)
                where.and(restroom.doorType.in(filter.getRestroomDoorType()));
            if (filter.getRestroomDoorWidthMin() != null)
                where.and(restroom.doorWidth.goe(filter.getRestroomDoorWidthMin()));
            if (filter.getRestroomDoorHeightMin() != null)
                where.and(restroom.doorHeight.goe(filter.getRestroomDoorHeightMin()));
        }

        // ELEVATOR 전용 필터
        boolean isElevator = filter.getFacilityType() != null &&
                filter.getFacilityType().contains(FacilityType.ELEVATOR);

        if (isElevator) {
            QElevator elevator = QElevator.elevator;
            query.leftJoin(elevator).on(elevator.facility.eq(facility)).fetchJoin();

            if (filter.getElevatorDoorHeightMin() != null) {
                where.and(elevator.doorHeight.goe(filter.getElevatorDoorHeightMin()));
            }
            if (filter.getElevatorDoorWidthMin() != null) {
                where.and(elevator.doorWidth.goe(filter.getElevatorDoorWidthMin()));
            }
            if (filter.getMaxControlPanelHeightMax() != null) {
                where.and(elevator.maxControlPanelHeight.loe(filter.getMaxControlPanelHeightMax()));
            }
        }

        // CLASSROOM 전용 필터
        boolean isClassroom = filter.getFacilityType() != null &&
                filter.getFacilityType().contains(FacilityType.CLASSROOM);

        if (isClassroom) {
            QClassroom classroom = QClassroom.classroom;
            query.leftJoin(classroom).on(classroom.facility.eq(facility)).fetchJoin();

            if (filter.getClassroomDoorHeightMin() != null) {
                where.and(classroom.doorHeight.goe(filter.getClassroomDoorHeightMin()));
            }
            if (filter.getClassroomDoorWidthMin() != null) {
                where.and(classroom.doorWidth.goe(filter.getClassroomDoorWidthMin()));
            }
            if (filter.getMinAisleWidthMin() != null) {
                where.and(classroom.minAisleWidth.goe(filter.getMinAisleWidthMin()));
            }
            if (filter.getHasThreshold() != null) {
                where.and(classroom.hasThreshold.eq(filter.getHasThreshold()));
            }
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

            } else if (f.getFacilityType() == FacilityType.ELEVATOR) {
                Elevator e = f.getElevator();
                return ElevatorStatsDto.builder()
                        .facilityId(f.getFacilityId())
                        .facilityType(f.getFacilityType())
                        .schoolId(s != null ? s.getSchoolId() : null)
                        .schoolName(schoolName)
                        .buildingName(buildingName)
                        .floorName(floorName)
                        .region(region)
                        .lastActivityAt(f.getUpdatedAt())

                        .doorWidth(e != null ? e.getDoorWidth() : null)
                        .minDoorWidth(e != null ? e.getMinDoorWidth() : null)
                        .maxDoorWidth(e != null ? e.getMaxDoorWidth() : null)
                        .doorHeight(e != null ? e.getDoorHeight() : null)
                        .maxControlPanelHeight(e != null ? e.getMaxControlPanelHeight() : null)

                        .build();

            } else if (f.getFacilityType() == FacilityType.CLASSROOM) {
                Classroom c = f.getClassroom();
                return ClassroomStatsDto.builder()
                        .facilityId(f.getFacilityId())
                        .facilityType(f.getFacilityType())
                        .schoolId(s != null ? s.getSchoolId() : null)
                        .schoolName(schoolName)
                        .buildingName(buildingName)
                        .floorName(floorName)
                        .region(region)
                        .lastActivityAt(f.getUpdatedAt())

                        .doorWidth(c != null ? c.getDoorWidth() : null)
                        .minDoorWidth(c != null ? c.getMinDoorWidth() : null)
                        .maxDoorWidth(c != null ? c.getMaxDoorWidth() : null)
                        .doorHeight(c != null ? c.getDoorHeight() : null)
                        .minAisleWidth(c != null ? c.getMinAisleWidth() : null)
                        .hasThreshold(c != null ? c.getHasThreshold() : null)
                        .doorType(c != null ? c.getDoorType() : null)

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