package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Restroom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RestroomResponseDto {
    private FacilityDetailResponseDto facilityDetail;
    private Float doorWidth;
    private String gender;
    private Boolean isAccessible;
    private String doorType;

    public static RestroomResponseDto from(Restroom restroom) {
        Facility facility = restroom.getFacility();
        return RestroomResponseDto.builder()
                .facilityDetail(FacilityDetailResponseDto.from(facility))
                .doorWidth(restroom.getDoorWidth())
                .gender(restroom.getGender())
                .isAccessible(restroom.getIsAccessible())
                .doorType(restroom.getDoorType())
                .build();
    }
}
