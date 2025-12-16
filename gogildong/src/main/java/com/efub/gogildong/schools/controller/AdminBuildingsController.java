package com.efub.gogildong.schools.controller;

import com.efub.gogildong.facility.dto.request.UpdateFloorPlanImageRequest;
import com.efub.gogildong.schools.dto.request.CreateBuildingRequest;
import com.efub.gogildong.schools.dto.request.CreateFloorRequest;
import com.efub.gogildong.schools.dto.request.UpdateBuildingRequest;
import com.efub.gogildong.schools.dto.response.BuildingListResponse;
import com.efub.gogildong.schools.dto.response.FloorPlanListResponse;
import com.efub.gogildong.schools.dto.response.FloorPlanResponse;
import com.efub.gogildong.schools.service.AdminBuildingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class AdminBuildingsController {

    private final AdminBuildingsService adminBuildingsService;

    /*
    * 학교 관리자가 해당 학교에 건물 추가
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createBuildings(@RequestBody @Valid CreateBuildingRequest request,
                                                                  Authentication authentication) {
        adminBuildingsService.createBuilding(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<Void> updateBuildingName(@RequestBody @Valid UpdateBuildingRequest request,
                                                   Authentication authentication) {
        adminBuildingsService.updateBuildingName(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    /*
    * 관리자가 속해있는 학교의 전체 건물 정보 조회
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<BuildingListResponse> getBuildings(Authentication authentication) {
        return ResponseEntity.ok(adminBuildingsService.getBuildingList(authentication.getName()));
    }

    /*
    * 층 별 도면 리스트 조회
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{buildingId}")
    public ResponseEntity<FloorPlanListResponse> getFloorPlansByBuilding(@PathVariable Long buildingId, Authentication authentication) {
        return ResponseEntity.ok(adminBuildingsService.getFloorPlanList(authentication.getName(), buildingId));
    }

    /*
    * 도면 상세 조회
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/floorplan/{floorId}")
    public ResponseEntity<FloorPlanResponse> getFloorPlanByFloorId(@PathVariable Long floorId, Authentication authentication) {
        return ResponseEntity.ok(adminBuildingsService.getFloorPlan(authentication.getName(), floorId));
    }

    /*
    *층 및 도면 추가
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/floors")
    public ResponseEntity<Void> createFloor(@RequestBody @Valid CreateFloorRequest request, Authentication authentication) {
        adminBuildingsService.createFloor(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    * 도면 수정하기
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/floorplan")
    public ResponseEntity<Void> updateFloorPlan(@RequestBody @Valid UpdateFloorPlanImageRequest request, Authentication authentication){
        adminBuildingsService.updateFloorPlan(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    /*
    * 건물 삭제
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{buildingId}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long buildingId, Authentication authentication) {
        adminBuildingsService.deleteBuilding(authentication.getName(), buildingId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteBuildingsInBuilding(Authentication authentication) {
        adminBuildingsService.deleteBuildings(authentication.getName());
        return ResponseEntity.ok().build();
    }

}
