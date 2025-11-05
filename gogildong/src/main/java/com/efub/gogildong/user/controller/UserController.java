package com.efub.gogildong.user.controller;

import com.efub.gogildong.user.dto.request.CreateAdminUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateExternalUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateInternalUserRequestDto;
import com.efub.gogildong.user.dto.request.UpdateInternalSchoolRequestDto;
import com.efub.gogildong.user.dto.response.InternalUserResponseDto;
import com.efub.gogildong.user.dto.response.CreateUserResponseDto;
import com.efub.gogildong.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 내부인 생성: POST /users/signup/internal
    @PostMapping("/signup/internal")
    public ResponseEntity<InternalUserResponseDto> createInternalUser(@RequestBody @Valid CreateInternalUserRequestDto requestDto) {
        InternalUserResponseDto responseDto = userService.createInternalUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    // 내부인 학교 변경: PATCH /users/me/school
    @PreAuthorize("hasAnyRole('INTERNAL')")
    @PatchMapping("/me/school")
    public ResponseEntity<InternalUserResponseDto> updateInternalSchool(
            org.springframework.security.core.Authentication authentication,
            @RequestBody @jakarta.validation.Valid UpdateInternalSchoolRequestDto request) {

        String loginId = authentication.getName(); // JwtFilter에서 set한 username (loginId)
        InternalUserResponseDto dto =
                userService.updateInternalUserSchoolByLoginId(loginId, request.getSchoolCode());
        return ResponseEntity.ok(dto);
    }

    // 외부인 생성: POST /users/signup/external
    @PostMapping("/signup/external")
    public ResponseEntity<CreateUserResponseDto> createExternalUser(@RequestBody @Valid CreateExternalUserRequestDto requestDto) {
        CreateUserResponseDto responseDto = userService.createExternalUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 학교 관리자 생성: POST /users/signup/admin
    @PostMapping("/signup/admin")
    public ResponseEntity<InternalUserResponseDto> createAdminUser(@RequestBody @Valid CreateAdminUserRequestDto requestDto) {
        InternalUserResponseDto responseDto = userService.createAdminUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


}
