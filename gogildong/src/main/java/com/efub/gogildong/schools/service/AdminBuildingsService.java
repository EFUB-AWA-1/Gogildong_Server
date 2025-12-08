package com.efub.gogildong.schools.service;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.respository.BuildingRepository;
import com.efub.gogildong.facility.respository.FloorRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.dto.request.CreateBuildingRequest;
import com.efub.gogildong.schools.dto.request.UpdateBuildingRequest;
import com.efub.gogildong.schools.dto.response.BuildingListResponse;
import com.efub.gogildong.schools.dto.response.BuildingSummaryResponse;
import com.efub.gogildong.schools.dto.response.FloorPlanListResponse;
import com.efub.gogildong.schools.dto.response.FloorPlanResponse;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBuildingsService {
    private final EntityFinder finder;
    private final BuildingRepository buildingRepository;
    private final FloorRepository floorRepository;

    /*
     * 학교 관리자가 해당 학교에 건물 추가
     * */
    @Transactional
    public void createBuilding(String loginId, CreateBuildingRequest request) {
        User user = finder.getUserByLoginId(loginId);
        Building createdBuilding = request.createBuilding();
        School school = user.getSchool();
        school.addBuilding(createdBuilding);
    }

    /*
    * 건물 이름 변경
    * */
    @Transactional
    public void updateBuildingName(String loginId, UpdateBuildingRequest request){
        User user = finder.getUserByLoginId(loginId);
        School school = user.getSchool();
        Building building = findBuildingByBuildingId(request.getBuildingId());
        if(building.getSchool() != school){
            throw new GoGildongException(ExceptionCode.NOT_ADMIN);
        }
        building.updateBuildingName(request.getBuildingName());
    }

    /*
    * 건물 리스트 조회
    * */
    public BuildingListResponse getBuildingList(String loginId) {
        User user = finder.getUserByLoginId(loginId);
        School school = user.getSchool();
        List<Building> buildings = buildingRepository.findBySchool(school);
        List<BuildingSummaryResponse> summaryResponses = buildings.stream().map(BuildingSummaryResponse::from).toList();
        return new BuildingListResponse(summaryResponses);
    }

    /*
    * 층별 도면 조회
    * */
    public FloorPlanListResponse getFloorPlanList(String loginId, Long buildingId) {
        User user = finder.getUserByLoginId(loginId);
        School school = user.getSchool();
        Building building = findBuildingByBuildingId(buildingId);
        List<Floor> floors = floorRepository.findByBuilding(building);
        List<FloorPlanResponse> floorPlanResponses = floors.stream().map(FloorPlanResponse::to).toList();
        return new FloorPlanListResponse(floorPlanResponses);
    }

    private Building findBuildingByBuildingId(Long buildingId) {
        return buildingRepository.findByBuildingId(buildingId).orElseThrow(() -> new GoGildongException(ExceptionCode.BUILDING_NOT_FOUND));
    }


}
