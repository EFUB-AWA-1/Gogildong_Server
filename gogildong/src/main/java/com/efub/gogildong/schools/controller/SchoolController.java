package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.domain.TagName;
import com.efub.gogildong.schools.domain.constants.NearbySearchDefaults;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.SchoolListResponse;
import com.efub.gogildong.schools.service.SchoolService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SchoolListResponse> getNearbySchools(@RequestParam(name = "lat", defaultValue = NearbySearchDefaults.LATITUDE + "") double latitude,
                                                               @RequestParam(name = "lng", defaultValue = NearbySearchDefaults.LONGITUDE + "") double longitude,
                                                               @RequestParam(name = "tag", defaultValue = "all") TagCategory tagCategory,
                                                               @RequestParam(name = "radius", defaultValue = NearbySearchDefaults.RADIUS + "") double radius) {
        return ResponseEntity.ok(schoolService.getNearbySchools(latitude, longitude, tagCategory, radius));
    }
}
