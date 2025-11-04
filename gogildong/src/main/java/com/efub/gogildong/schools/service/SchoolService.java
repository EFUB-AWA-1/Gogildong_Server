package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.dto.response.SchoolListResponse;
import com.efub.gogildong.schools.dto.response.SchoolSummaryResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                                               double radius,
                                               Pageable pageable) {
        // param으로 들어온 tag category 값을 tag 이름으로 변환
        String tagFilter = tagCategory == TagCategory.all ? null : tagCategory.toTagName().name();

        // 위도, 경도, 태그, 반경을 기준으로 학교 정보 조회
        Page<School> pageOfSchools = schoolRepository.findSchoolsWithinRadiusAndTag(latitude, longitude, radius, tagFilter, pageable);
        List<School> schools = pageOfSchools.getContent();
        // 검색 결과가 없을 때 404 에러
        ensureSchoolExists(schools);

        // 학교를 DTO로 변환
        List<SchoolSummaryResponse> schoolSummaryResponses = SchoolSummaryResponse.fromEntityList(schools);

        return SchoolListResponse.builder()
                .totalPages(pageOfSchools.getTotalPages())
                .totalElements(pageOfSchools.getTotalElements())
                .last(pageOfSchools.isLast())
                .schools(schoolSummaryResponses)
                .build();
    }

    /*
    * 검색어 기반 학교 정보를 조회합니다.
    * */
    @Transactional(readOnly = true)
    public SchoolListResponse getSchoolsByQuery(String query, Pageable pageable) {
        Page<School> pageOfSchools = schoolRepository.searchByQuery(query, pageable);
        List<School> schools = pageOfSchools.getContent();
        ensureSchoolExists(schools);
        List<SchoolSummaryResponse> schoolSummaryResponses = SchoolSummaryResponse.fromEntityList(schools);

        return SchoolListResponse.builder()
                .last(pageOfSchools.isLast())
                .totalElements(pageOfSchools.getTotalElements())
                .totalPages(pageOfSchools.getTotalPages())
                .schools(schoolSummaryResponses)
                .build();
    }

    /*
    * 검색 결과에 학교가 포함되는 것을 보장합니다.
    * */
    private void ensureSchoolExists(List<School> schools) {
        if(schools.isEmpty()) {
            throw new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND);
        }
    }

    /*
    * 학교 id로 상세 정보를 조회합니다.
    * */
    @Transactional(readOnly = true)
    public SchoolSummaryResponse getSchoolInfoById(Long schoolId) {
        School school = getSchoolById(schoolId);
        return SchoolSummaryResponse.fromEntity(school);
    }

    /*
    * 학교 아이디로 학교 조회
    * */
    @Transactional(readOnly = true)
    public School getSchoolById(Long schoolId) {
        return schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));
    }
}
