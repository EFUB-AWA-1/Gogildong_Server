package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.dto.request.AdminAddSchoolRequest;
import com.efub.gogildong.schools.dto.response.AdminAddSchoolResponse;
import com.efub.gogildong.schools.dto.response.AdminSchoolListResponse;
import com.efub.gogildong.schools.service.AdminSchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/schools")
public class AdminSchoolController {

    private final AdminSchoolService adminSchoolService;

    // 등록된 학교 리스트 조회
    @GetMapping
    public ResponseEntity<AdminSchoolListResponse> getSchools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        AdminSchoolListResponse response = adminSchoolService.getSchools(page, size, token);
        return ResponseEntity.ok(response);
    }

    // 학교 검색
    @GetMapping("/search")
    public ResponseEntity<AdminSchoolListResponse> searchSchools(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        AdminSchoolListResponse response = adminSchoolService.searchSchools(region, level, keyword, page, size, token);
        return ResponseEntity.ok(response);
    }

    // 학교 추가
    @PostMapping("/add")
    public ResponseEntity<AdminAddSchoolResponse> addSchool(
            @RequestBody AdminAddSchoolRequest request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        AdminAddSchoolResponse response = adminSchoolService.addSchool(request, token);
        return ResponseEntity.ok(response);
    }
}
