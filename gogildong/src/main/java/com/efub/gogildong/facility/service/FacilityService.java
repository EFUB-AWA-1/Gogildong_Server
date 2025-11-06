package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.facility.dto.response.FacilityDetailResponse;
import com.efub.gogildong.facility.dto.response.RestroomResponse;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final EntityFinder entityFinder;
    private final SchoolViewRequestService schoolViewRequestService;

    // 시설 상세 조회
    @Transactional(readOnly = true)
    public Object getFacilityDetail(String loginId, Long facilityId) {
        User user = entityFinder.getUserByLoginId(loginId);
        Facility facility = entityFinder.getFacilityById(facilityId);
        School school = entityFinder.getSchoolByFacility(facility);

        // 열람 권한 여부 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // facilityType으로 분기
        // 각 타입별 Dto 변환 메서드 호출
        switch (facility.getFacilityType()) {
            case RESTROOM :
                return RestroomResponse.from(facility.getRestroom());
            default:
                return FacilityDetailResponse.from(facility);
        }
    }
}
