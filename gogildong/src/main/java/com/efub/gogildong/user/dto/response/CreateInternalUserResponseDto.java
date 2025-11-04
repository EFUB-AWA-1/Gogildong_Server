package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
public class CreateInternalUserResponseDto {

    private Long userId;
    private UserRole role;
    private String schoolCode;
    private String schoolName;

    public static CreateInternalUserResponseDto of(User user, School school) {
        return CreateInternalUserResponseDto.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .schoolCode(school != null ? school.getSchoolCode() : null)
                .schoolName(school != null ? school.getSchoolName() : null)
                .build();
    }

    public static CreateInternalUserResponseDto from(User user) {
        School s = user.getSchool();
        return of(user, s);
    }
}
