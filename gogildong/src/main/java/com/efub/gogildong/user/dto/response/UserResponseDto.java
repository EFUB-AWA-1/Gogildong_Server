package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String loginId;
    private String username;
    private UserRole role;
    private String email;
    private String phone;
    private String schoolCode;
    private String schoolName;

    public static UserResponseDto from(User user) {
        School s = user.getSchool();
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .role(user.getRole())
                .email(user.getEmail())
                .phone(user.getPhone())
                .schoolCode(s != null ? s.getSchoolCode() : null)
                .schoolName(s != null ? s.getSchoolName() : null)
                .build();
    }
}