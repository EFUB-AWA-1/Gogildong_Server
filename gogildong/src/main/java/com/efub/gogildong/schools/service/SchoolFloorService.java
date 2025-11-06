package com.efub.gogildong.schools.service;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.respository.BuildingRepository;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.FloorRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.*;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolFloorService {
    private final SchoolViewRequestService schoolViewRequestService;
    private final EntityFinder finder;

    /*
    * 학교 id로 해당 학교에 존재하는 층을 조회합니다.
    * */
    @Transactional(readOnly = true)
    public FloorListResponse getAllFloorsBySchoolId(long schoolId) {
        // 학교 조회
        School school = finder.getSchoolById(schoolId);

        // 학교에 속한 건물 조회
        List<Building> buildings = finder.getBuildingBySchool(school);

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
    public FacilityListResponse getAllFacilitiesByFloorId(String loginId,
                                                          Long schoolId,
                                                          Long floorId,
                                                          TagCategory tagCategory) {
        User user = finder.getUserByLoginId(loginId);
        School school = finder.getSchoolById(schoolId);
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        Floor floor = finder.getFloorById(floorId);

        getValidatedSchoolByFloorId(floor, school);

        List<Facility> facilities = finder.getAllFacilityByFloorAndType(floor, tagCategory);
        List<FacilitySummaryResponse> facilitySummaryResponses = facilities.stream().map(FacilitySummaryResponse::from).toList();
        return new FacilityListResponse(facilitySummaryResponses.size(), facilitySummaryResponses);
    }

    /*
    * 층 별 도면을 조회합니다.
    * */
    @Transactional(readOnly = true)
    public FloorPlanImageResponse getFloorPlanImageByFloorId(String loginId, Long schoolId, Long floorId) {
        User user = finder.getUserByLoginId(loginId);
        Floor floor = finder.getFloorById(floorId);
        School school = finder.getSchoolById(schoolId);
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);
        getValidatedSchoolByFloorId(floor, school);
        return FloorPlanImageResponse.from(floor);
    }

    /*
    * 해당 학교에 해당 층이 존재하는지 확인합니다.
    * */
    private void getValidatedSchoolByFloorId(Floor floor, School school) {
        Building building = finder.getBuildingByFloor(floor);
        if(!building.getSchool().equals(school)) {
            throw new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND_IN_SCHOOL);
        }
    }

}
