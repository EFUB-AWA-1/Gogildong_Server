package com.efub.gogildong.reports.controller;

import com.efub.gogildong.facility.dto.response.FacilityListResponse;
import com.efub.gogildong.reports.service.SelectionLocationService;
import com.efub.gogildong.schools.dto.response.BuildingListResponse;
import com.efub.gogildong.schools.dto.response.FloorListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SelectLocationController {

    private final SelectionLocationService selectionLocationService;

    /*
    * 사용자의 학교 별 건물 조회
    * */
    @GetMapping("/buildings")
    public ResponseEntity<BuildingListResponse> getBuildingList(Authentication authentication) {
        return ResponseEntity.ok(selectionLocationService.getBuildingList(authentication.getName()));
    }

    /*
    * 건물 별 층 조회
    * */
    @GetMapping("/buildings/{buildingId}/floors")
    public ResponseEntity<FloorListResponse> getFloorListInBuilding(@PathVariable Long buildingId, Authentication authentication) {
        return ResponseEntity.ok(selectionLocationService.getFloorListInBuilding(authentication.getName(), buildingId));
    }

    /*
    * 층 별 시설 조회
    * */
    @GetMapping("/floors/{floorId}/facilities")
    public ResponseEntity<FacilityListResponse> getFacilityListInBuilding(@PathVariable Long floorId, Authentication authentication, @RequestParam(name = "type") String type) {
        return ResponseEntity.ok(selectionLocationService.getFacilityList(authentication.getName(), floorId, type));
    }


}
