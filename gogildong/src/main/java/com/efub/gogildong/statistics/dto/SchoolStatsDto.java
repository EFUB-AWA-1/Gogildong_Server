package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.schools.domain.EduLevel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class SchoolStatsDto {
    private Long schoolId;
    private String schoolName;
    private EduLevel schoolLevel;
    private String region;
    private Boolean hasSpecialClass;
    private Integer studentCount;
    private LocalDateTime lastActivityAt;

    @QueryProjection
    public SchoolStatsDto(Long schoolId, String schoolName, EduLevel eduLevel,
                          String region, Boolean hasSpecialClass, Integer studentCount, LocalDateTime lastActivityAt) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolLevel = eduLevel;
        this.region = region;
        this.hasSpecialClass = hasSpecialClass;
        this.studentCount = studentCount;
        this.lastActivityAt = lastActivityAt;
    }
}
