package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.dto.request.CreateBuildingRequest;
import com.efub.gogildong.schools.dto.request.UpdateBuildingRequest;
import com.efub.gogildong.schools.dto.response.BuildingListResponse;
import com.efub.gogildong.schools.dto.response.BuildingSummaryResponse;
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

}
