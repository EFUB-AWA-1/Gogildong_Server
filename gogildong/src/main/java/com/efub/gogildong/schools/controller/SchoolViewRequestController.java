package com.efub.gogildong.schools.controller;

import com.efub.gogildong.auth.dto.CustomUserDetails;
import com.efub.gogildong.schools.dto.request.SchoolViewRequestRequest;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestResponse;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
