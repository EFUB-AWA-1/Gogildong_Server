package com.efub.gogildong.facility.dto.response;

import com.efub.gogildong.facility.domain.Etc;
import com.efub.gogildong.facility.domain.Facility;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtcResponse {
    private FacilityDetailResponse facilityDetail;

    public static EtcResponse from(Etc etc) {
        Facility facility = etc.getFacility();
        return EtcResponse.builder()
                .facilityDetail(FacilityDetailResponse.from(facility))
                .build();
    }
}
