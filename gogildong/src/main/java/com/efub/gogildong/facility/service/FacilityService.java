package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.dto.response.FacilityDetailResponse;
import com.efub.gogildong.facility.dto.response.RestroomResponse;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    // 시설 상세 조회
    @Transactional(readOnly = true)
    public Object getFacilityDetail(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));

        // facilityType으로 분기
        // 각 타입별 Dto 변환 메서드 호출
        switch (facility.getFacilityType()) {
            case "restroom":
                return RestroomResponse.from(facility.getRestroom());
            default:
                return FacilityDetailResponse.from(facility);
        }
    }
}
