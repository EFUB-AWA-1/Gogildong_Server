package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.dto.response.AdminSchoolListResponse;
import com.efub.gogildong.schools.service.AdminSchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/schools")
public class AdminSchoolController {

    private final AdminSchoolService adminSchoolService;

    // 등록된 학교 리스트 조회
    @GetMapping
    public ResponseEntity<AdminSchoolListResponse> getSchools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        AdminSchoolListResponse response = adminSchoolService.getSchools(page, size);
        return ResponseEntity.ok(response);
    }

    // 학교 검색
    @GetMapping("/search")
    public ResponseEntity<AdminSchoolListResponse> searchSchools(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        AdminSchoolListResponse response = adminSchoolService.searchSchools(region, level, keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
