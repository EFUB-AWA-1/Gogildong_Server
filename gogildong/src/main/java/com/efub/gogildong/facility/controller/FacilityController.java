package com.efub.gogildong.facility.controller;

import com.efub.gogildong.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facilities/{facility_id}")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    // 시설 상세 조회
    @GetMapping
    public ResponseEntity<?> getFacilityDetail(Authentication authentication,
                                               @PathVariable Long facility_id) {
        // 시설 타입에 따라 Dto가 달라지므로 반환타입 Object
        Object response = facilityService.getFacilityDetail(authentication.getName(), facility_id);
        return ResponseEntity.ok(response);
    }

    // 시설 이미지 조회


    // 시설 이미지 신고

}
