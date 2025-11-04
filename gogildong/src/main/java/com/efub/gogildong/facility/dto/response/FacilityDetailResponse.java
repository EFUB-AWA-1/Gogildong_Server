package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FacilityDetailResponse {
    private String buildingName;
    private String floorName;
    private Long facilityId;
    private String facilityName;
    private String facilityNiceName;
    private String facilityType;
    private String reviewSummary;
    private LocalDateTime createdAt;

    public static FacilityDetailResponse from(Facility facility) {
        return FacilityDetailResponse.builder()
                .buildingName(facility.getFloor().getBuilding().getBuildingName())
                .floorName(facility.getFloor().getFloorName())
                .facilityId(facility.getFacilityId())
                .facilityName(facility.getFacilityName())
                .facilityNiceName(facility.getFacilityNickname())
                .facilityType(facility.getFacilityType())
                .reviewSummary(facility.getReviewSummary())
                .createdAt(facility.getCreatedAt())
                .build();
    }

}
