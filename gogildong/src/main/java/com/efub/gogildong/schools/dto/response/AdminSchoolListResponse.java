package com.efub.gogildong.schools.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AdminSchoolListResponse {

    private int total;
    private List<SchoolDto> schools;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SchoolDto {
        private String region;
        private String eduLevel;
        private String schoolName;
        private String schoolCode;
        private String status;
        private String adminPhone;
        private LocalDateTime registeredAt;
    }
}