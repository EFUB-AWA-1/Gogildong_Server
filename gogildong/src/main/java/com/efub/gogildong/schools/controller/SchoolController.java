package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.domain.constants.NearbySearchDefaults;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.SchoolListResponse;
import com.efub.gogildong.schools.dto.response.SchoolSummaryResponse;
import com.efub.gogildong.schools.service.SchoolService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    /*
    * 위도, 경도 기반 근처 학교 리스트 조회
    * tag(all, restroom, elevator, ramp)로 필터
    * 반경의 단위는 m
    * */
    @GetMapping("/nearby")
    public ResponseEntity<SchoolListResponse> getSchoolsByNear(Authentication authentication,
                                                               @RequestParam(name = "lat", defaultValue = NearbySearchDefaults.LATITUDE + "") double latitude,
                                                               @RequestParam(name = "lng", defaultValue = NearbySearchDefaults.LONGITUDE + "") double longitude,
                                                               @RequestParam(name = "tag", defaultValue = "all") TagCategory tagCategory,
                                                               @RequestParam(name = "radius", defaultValue = NearbySearchDefaults.RADIUS + "") double radius,
                                                               @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(schoolService.getNearbySchools(authentication.getName(), latitude, longitude, tagCategory, radius, pageable));
    }

    /*
    * 검색어 기반 학교 리스트 조회
    * */
    @GetMapping("/search")
    public ResponseEntity<SchoolListResponse> getSchoolsByQuery(Authentication authentication,
                                                                @RequestParam("query") String query,
                                                                @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(schoolService.getSchoolsByQuery(authentication.getName(), query, pageable));
    }

    /*
    * 학교 정보 상세 조회
    * */
    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolSummaryResponse> getSchoolInfo(Authentication authentication, @PathVariable long schoolId) {
        return ResponseEntity.ok(schoolService.getSchoolInfoById(authentication.getName(), schoolId));
    }
}
