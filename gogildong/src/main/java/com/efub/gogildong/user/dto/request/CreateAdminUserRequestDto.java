package com.efub.gogildong.user.dto.request;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.efub.gogildong.user.dto.common.BaseUserRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAdminUserRequestDto extends BaseUserRequestDto {

    @NotBlank
    private String schoolCode;

    @NotBlank
    private String adminCode;

    @JsonIgnore
    private final UserRole role = UserRole.ADMIN;

    public User toEntity() {
        return User.builder()
                .loginId(getLoginId())
                .password(getPassword())
                .username(getUsername())
                .email(getEmail())
                .phone(getPhone())
                .role(getRole())
                .build();
    }
}
