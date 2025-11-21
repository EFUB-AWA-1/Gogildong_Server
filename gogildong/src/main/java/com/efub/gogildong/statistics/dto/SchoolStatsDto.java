package com.efub.gogildong.statistics.dto;

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
    private String schoolLevel;
    private String region;
    private Boolean hasSpecialClass;
    private Integer studentCount;
    private LocalDateTime lastActivityAt;
}
