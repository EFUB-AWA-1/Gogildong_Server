package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.schools.domain.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SchoolSummaryResponse {
    private Long schoolId;
    private String schoolName;
    private String image;
    private String address;
    private double latitude;
    private double longitude;
    private List<String> tag;
    private boolean isLike;

    public static SchoolSummaryResponse fromEntity(School school) {
        List<String> tagList = school.getSchoolTags().stream()
                .map(st -> String.valueOf(st.getTagName()))
                .toList();

        return SchoolSummaryResponse.builder()
                .schoolId(school.getSchoolId())
                .schoolName(school.getSchoolName())
                .address(school.getAddress())
                .latitude(school.getLocation().getY())
                .longitude(school.getLocation().getX())
                .tag(tagList)
                .build();
    }
}
