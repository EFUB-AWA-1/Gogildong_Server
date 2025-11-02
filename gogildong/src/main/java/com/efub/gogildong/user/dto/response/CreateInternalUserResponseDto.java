package com.efub.gogildong.user.dto.response;

import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateInternalUserResponseDto {

    private Long userId;
    private UserRole role;
    private String schoolCode;
    private String schoolName;

//    public static CreateUserResponseDto from(User user) {
//        return CreateUserResponseDto.builder()
//                .userId(user.getUserId())
//                .role(user.getRole())
//                .build();
//    }

    // User + School을 함께 받는 팩토리 (권장)
    public static CreateInternalUserResponseDto of(User user, School school) {
        return CreateInternalUserResponseDto.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .schoolCode(school != null ? school.getSchoolCode() : null)
                .schoolName(school != null ? school.getSchoolName() : null)
                .build();
    }

    // 기존 시그니처도 유지하고 싶다면 (트랜잭션 내에서만 안전)
    public static CreateInternalUserResponseDto from(User user) {
        School s = user.getSchool(); // LAZY 주의
        return of(user, s);
    }
}
