package com.efub.gogildong.schools.controller;

import com.efub.gogildong.schools.dto.request.SchoolViewRequestRequest;
import com.efub.gogildong.schools.dto.request.UpdateSchoolViewRequestStatusRequest;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestDetailResponse;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestListResponse;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestResponse;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/view-requests")
@RequiredArgsConstructor
public class SchoolViewRequestController {

    private final UserRepository userRepository;
    private final SchoolViewRequestService schoolViewRequestService;

    // 학교 정보 열람 신청
    @PostMapping
    public ResponseEntity<SchoolViewRequestResponse> createSchoolViewRequest(Authentication authentication,
                                                                             @RequestBody @Valid SchoolViewRequestRequest request) {
        SchoolViewRequestResponse response = schoolViewRequestService.createSchoolViewRequest(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 학교 정보 열람 신청 상세 조회 (학교 관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{requestId}")
    public ResponseEntity<SchoolViewRequestDetailResponse> getRequestDetail(Authentication authentication,
                                                                            @PathVariable final Long requestId) {

        SchoolViewRequestDetailResponse response = schoolViewRequestService.getRequestDetail(requestId);
        return ResponseEntity.ok(response);
    }

    // 학교 정보 열람 신청 목록 조회 (학교 관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<SchoolViewRequestListResponse> getAllRequests(Authentication authentication) {
        SchoolViewRequestListResponse response = schoolViewRequestService.getAllRequests();
        return ResponseEntity.ok(response);
    }

    // 학교 정보 열람 요청 상태 수정 (학교 관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{requestId}")
    public ResponseEntity<Void> updateRequestStatus(Authentication authentication,
                                                    @PathVariable final Long requestId,
                                                    @RequestBody @Valid final UpdateSchoolViewRequestStatusRequest requestDto) {

        schoolViewRequestService.updateSchoolViewRequestStatus(requestId, requestDto);
        return ResponseEntity.noContent().build();
    }
}
