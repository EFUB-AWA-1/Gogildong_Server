package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AdminAddSchoolResponse {
    private Long schoolId;
    private String schoolCode;
    private String schoolName;
    private String address;
    private String eduLevel;
    private String adminCode;
    private Boolean hasSpecialClass;
    private String region;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
