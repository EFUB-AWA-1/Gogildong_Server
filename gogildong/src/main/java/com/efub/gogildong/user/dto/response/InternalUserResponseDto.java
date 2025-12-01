package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InternalUserResponseDto {

    private Long userId;
    private String username;
    private UserRole role;
    private Long schoolId;
    private String schoolCode;
    private String schoolName;

    public static InternalUserResponseDto of(User user, School school) {
        return InternalUserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole())
                .schoolId(school != null ? school.getSchoolId() : null)
                .schoolCode(school != null ? school.getSchoolCode() : null)
                .schoolName(school != null ? school.getSchoolName() : null)
                .build();
    }

    public static InternalUserResponseDto from(User user) {
        School s = user.getSchool();
        return of(user, s);
    }
}
