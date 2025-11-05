package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.dto.response.FacilityDetailResponse;
import com.efub.gogildong.facility.dto.response.RestroomResponse;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolViewRequestService schoolViewRequestService;

    // 시설 상세 조회
    @Transactional(readOnly = true)
    public Object getFacilityDetail(String loginId, Long facilityId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));

        School school = schoolRepository.findBySchoolId(facility.getFloor().getBuilding().getSchool().getSchoolId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // 열람 권한 여부 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

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
