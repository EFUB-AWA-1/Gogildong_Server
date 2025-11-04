package com.efub.gogildong.schools.service;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.respository.BuildingRepository;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.FloorRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.FacilityListResponse;
import com.efub.gogildong.schools.dto.response.FacilitySummaryResponse;
import com.efub.gogildong.schools.dto.response.FloorListResponse;
import com.efub.gogildong.schools.dto.response.FloorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolFloorService {
    private final SchoolService schoolService;
    private final BuildingRepository buildingRepository;
    private final FloorRepository floorRepository;
    private final FacilityRepository facilityRepository;

    /*
    * 학교 id로 해당 학교에 존재하는 층을 조회합니다.
    * */
    @Transactional(readOnly = true)
    public FloorListResponse getAllFloorsBySchoolId(long schoolId) {
        // 학교 조회
        School school = schoolService.getSchoolById(schoolId);

        // 학교에 속한 건물 조회
        List<Building> buildings = buildingRepository.findBySchool(school);

        // 각 건물에 있는 층 조회 후 리스트 생성
        List<FloorResponse> floorResponses = buildings.stream()
                .flatMap(b -> b.getFloors().stream()
                        .map(f -> new FloorResponse(
                                f.getFloorId(),
                                b.getBuildingName() + " " + f.getFloorName() + "층"
                        ))
                )
                .toList();

        return new FloorListResponse(floorResponses, floorResponses.size());
    }

    /*
    * 층 id를 이용해 타입 별로 층에 존재하는 시설을 조회합니다.
    * */
    @Transactional(readOnly = true)
    public FacilityListResponse getAllFacilitiesByFloorId(Long schoolId,
                                                          Long floorId,
                                                          TagCategory tagCategory) {
        School school = schoolService.getSchoolById(schoolId);

        // TODO: 추후 회원 유효성 검사 필요

        Floor floor = getFloorById(floorId);

        Building building = buildingRepository
                .findByFloor(floor).orElseThrow(()-> new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND));
        if(!building.getSchool().equals(school)) {
            throw new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND_IN_SCHOOL);
        }

        List<Facility> facilities = facilityRepository.findAllByFloorAndType(floor, tagCategory.name());
        List<FacilitySummaryResponse> facilitySummaryResponses = facilities.stream().map(FacilitySummaryResponse::from).toList();
        return new FacilityListResponse(facilitySummaryResponses.size(), facilitySummaryResponses);
    }

    /*
    * 층 id로 floor를 조회합니다.
    * */
    private Floor getFloorById(Long floorId) {
        return floorRepository.findByFloorId(floorId)
                .orElseThrow(()-> new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND));
    }
}
