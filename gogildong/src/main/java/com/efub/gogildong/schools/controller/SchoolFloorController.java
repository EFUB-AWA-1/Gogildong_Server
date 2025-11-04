package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.FacilityListResponse;
import com.efub.gogildong.schools.dto.response.FloorListResponse;
import com.efub.gogildong.schools.service.SchoolFloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolFloorController {

    private final SchoolFloorService schoolFloorService;

    /*
    * 교내 층 리스트 조회
    * */
    @GetMapping("/{schoolId}/floors")
    public ResponseEntity<FloorListResponse> getFloorListBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(schoolFloorService.getAllFloorsBySchoolId(schoolId));
    }

    /*
    * 타입 별 층에 존재하는 시설 조회
    * */
    @GetMapping("/{schoolId}/floors/{floorId}")
    public ResponseEntity<FacilityListResponse> getFacilityListByFloorId(@PathVariable Long schoolId,
                                                                         @PathVariable Long floorId,
                                                                         @RequestParam(name = "type", defaultValue = "all") TagCategory type) {
        return ResponseEntity.ok(schoolFloorService.getAllFacilitiesByFloorId(schoolId, floorId, type));
    }

}
