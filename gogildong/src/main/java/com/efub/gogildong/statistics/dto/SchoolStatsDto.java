package com.efub.gogildong.statistics.dto;

import com.efub.gogildong.schools.domain.EduLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolStatsDto {
    private Long schoolId;
    private String schoolName;
    private EduLevel schoolLevel;
    private String region;
    private Boolean hasSpecialClass;
    private Integer studentCount;
    private LocalDateTime lastActivityAt;
}
