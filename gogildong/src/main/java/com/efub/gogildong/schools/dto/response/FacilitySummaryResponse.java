package com.efub.gogildong.schools.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class FacilitySummaryResponse {
    private Long facilityId;
    private String facilityName;
    private String facilityNickname;
    private String facilityImage;
    private boolean isAccessible;
    private String facilityType;
    private LocalDateTime updateAt;

    public static FacilitySummaryResponse from(Facility facility) {
        // TODO: 제보 테이블 만들면서 image 추가하기
        // TODO: 제보 테이블 만들면서 isAccessible 추가하기
        return FacilitySummaryResponse.builder()
                .facilityId(facility.getFacilityId())
                .facilityName(facility.getFacilityName())
                .facilityNickname(facility.getFacilityNickname())
                .facilityType(facility.getFacilityType())
                .build();
    }
}
