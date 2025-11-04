package com.efub.gogildong.user.dto.request;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.efub.gogildong.user.dto.common.BaseUserRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateExternalUserRequestDto extends BaseUserRequestDto {

    @JsonIgnore
    private final UserRole role = UserRole.EXTERNAL;

    public User toEntity() {
        return User.builder()
                .loginId(getLoginId())
                .username(getUsername())
                .email(getEmail())
                .phone(getPhone())
                .role(getRole())
                .build();
    }

}
