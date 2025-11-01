package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.SchoolListResponse;
import com.efub.gogildong.schools.dto.response.SchoolSummaryResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    /*
    * 위도 경도 기반 반경 n 미터 학교 정보를 조회합니다.
    * */
    @Transactional(readOnly = true)
    public SchoolListResponse getNearbySchools(double latitude,
                                               double longitude,
                                               TagCategory tagCategory,
                                               double radius) {
        // param으로 들어온 tag category 값을 tag 이름으로 변환
        String tagFilter = tagCategory == TagCategory.all ? null : tagCategory.toTagName().name();

        // 위도, 경도, 태그, 반경을 기준으로 학교 정보 조회
        List<School> nearbySchools = schoolRepository.findSchoolsWithinRadiusAndTag(latitude, longitude, radius, tagFilter);

        // 검색 결과가 없을 때 404 에러
        if (nearbySchools.isEmpty()) {
            throw new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND);
        }

        // 학교를 DTO로 변환
        List<SchoolSummaryResponse> schoolSummaryResponses = nearbySchools.stream()
                .map(SchoolSummaryResponse::fromEntity)
                .toList();

        return new SchoolListResponse(schoolSummaryResponses.size(), schoolSummaryResponses);
    }
}
