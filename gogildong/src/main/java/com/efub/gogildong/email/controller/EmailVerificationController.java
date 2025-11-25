package com.efub.gogildong.email.controller;

import com.efub.gogildong.email.dto.SendEmailCodeRequest;
import com.efub.gogildong.email.dto.SimpleMessageResponse;
import com.efub.gogildong.email.dto.VerifyEmailCodeRequest;
import com.efub.gogildong.email.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    /*
    POST /auth/email/code : 인증번호 발송
     */
    @PostMapping("/code")
    public ResponseEntity<SimpleMessageResponse> sendEmailCode(@RequestBody @Valid SendEmailCodeRequest request) {
        emailVerificationService.sendCode(request.getEmail());
        return ResponseEntity.ok(new SimpleMessageResponse("인증번호가 발송되었습니다."));
    }

    /*
    POST /auth/email/verify : 인증번호 확인
     */
    @PostMapping("/verify")
    public ResponseEntity<SimpleMessageResponse> verifyEmailCode(@RequestBody @Valid VerifyEmailCodeRequest request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok(new SimpleMessageResponse("이메일 인증이 완료되었습니다."));
    }
}
