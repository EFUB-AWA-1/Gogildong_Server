package com.efub.gogildong.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyEmailCodeRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String verificationCode;
}
