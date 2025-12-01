package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserResponseDto {

    private final Long userId;
    private final String username;
    private final UserRole role;

    public static CreateUserResponseDto from(User user) {
        return CreateUserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
