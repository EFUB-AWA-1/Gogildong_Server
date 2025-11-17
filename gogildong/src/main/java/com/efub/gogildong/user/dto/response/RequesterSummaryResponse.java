package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequesterSummaryResponse {

    private final Long userId;
    private final String username;
    private final UserRole role;
    private final String email;
    private final String phone;

    public static RequesterSummaryResponse from(User user) {
        return RequesterSummaryResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
