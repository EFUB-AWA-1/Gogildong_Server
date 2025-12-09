package com.efub.gogildong.reports.service;

import com.efub.gogildong.facility.domain.Building;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.dto.response.FacilityListResponse;
import com.efub.gogildong.facility.dto.response.FacilitySummaryResponse;
import com.efub.gogildong.facility.respository.BuildingRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.dto.response.BuildingListResponse;
import com.efub.gogildong.schools.dto.response.BuildingSummaryResponse;
import com.efub.gogildong.schools.dto.response.FloorListResponse;
import com.efub.gogildong.schools.dto.response.FloorResponse;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionLocationService {

    private final EntityFinder finder;
    private final BuildingRepository buildingRepository;

    @Transactional(readOnly = true)
    public BuildingListResponse getBuildingList(String loginId) {
        User user = finder.getUserByLoginId(loginId);
        School school = user.getSchool();
        List<Building> buildings = buildingRepository.findBySchool(school);
        List<BuildingSummaryResponse> summaryResponses = buildings.stream().map(BuildingSummaryResponse::from).toList();
        return new BuildingListResponse(summaryResponses);
    }

    @Transactional(readOnly = true)
    public FloorListResponse getFloorListInBuilding(String loginId, Long buildingId) {
        User user = finder.getUserByLoginId(loginId);
        Building building = buildingRepository.findByBuildingId(buildingId).orElseThrow(()-> new GoGildongException(ExceptionCode.BUILDING_NOT_FOUND));
        if(building.getSchool() != user.getSchool()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_SCHOOL_ACCESS);
        }
        List<FloorResponse> floors = building.getFloors().stream().map(FloorResponse::from).toList();
        return new FloorListResponse(floors, floors.size());
    }

    @Transactional(readOnly = true)
    public FacilityListResponse getFacilityList(String loginId, Long floorId) {
        User user = finder.getUserByLoginId(loginId);
        Floor floor = finder.getFloorById(floorId);
        if(floor.getBuilding().getSchool() != user.getSchool()) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_SCHOOL_ACCESS);
        }
        List<FacilitySummaryResponse> facilitySummaryResponses = floor.getFacilities().stream().map(FacilitySummaryResponse::from).toList();
        return new FacilityListResponse(facilitySummaryResponses, facilitySummaryResponses.size());
    }
}
