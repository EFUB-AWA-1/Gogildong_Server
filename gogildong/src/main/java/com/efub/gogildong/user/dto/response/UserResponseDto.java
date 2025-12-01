package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdAt;

    public static UserResponseDto userInfo(User user) {
        School school = user.getSchool();
        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .userId(user.getUserId())
                .role(user.getRole())
                .email(user.getEmail())
                .schoolCode(school.getSchoolCode())
                .schoolName(school.getSchoolName())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }

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