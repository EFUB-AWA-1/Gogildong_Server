package com.efub.gogildong.user.controller;

import com.efub.gogildong.user.dto.request.CreateInternalUserRequestDto;
import com.efub.gogildong.user.dto.response.CreateInternalUserResponseDto;
import com.efub.gogildong.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<CreateInternalUserResponseDto> createInternalUser(@RequestBody @Valid CreateInternalUserRequestDto requestDto) {
        CreateInternalUserResponseDto responseDto = userService.createInternalUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

}
