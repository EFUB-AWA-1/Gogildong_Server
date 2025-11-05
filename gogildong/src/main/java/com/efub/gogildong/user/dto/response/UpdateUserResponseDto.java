package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String phone;

    public static UpdateUserResponseDto from(User user) {
        return UpdateUserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}

