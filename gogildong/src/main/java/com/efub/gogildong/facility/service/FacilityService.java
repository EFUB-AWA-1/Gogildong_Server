package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.facility.dto.request.FacilityImageFlagRequest;
import com.efub.gogildong.facility.dto.response.FacilityDetailResponse;
import com.efub.gogildong.facility.dto.response.FacilityImageListResponse;
import com.efub.gogildong.facility.dto.response.FacilityImageSummaryResponse;
import com.efub.gogildong.facility.dto.response.RestroomResponse;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.reports.domain.ReportFlag;
import com.efub.gogildong.reports.repository.ReportFlagRepository;
import com.efub.gogildong.reports.repository.ReportRepository;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final EntityFinder entityFinder;
    private final SchoolViewRequestService schoolViewRequestService;
    private final ReportRepository reportRepository;
    private final ReportFlagRepository reportFlagRepository;

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

    // 시설 이미지 조회
    @Transactional(readOnly = true)
    public FacilityImageListResponse getFacilityImages(String loginId, Long facilityId) {
        User user = entityFinder.getUserByLoginId(loginId);
        Facility facility = entityFinder.getFacilityById(facilityId);
        School school = entityFinder.getSchoolByFacility(facility);

        // 시설 이미지 열람 권한 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // facilityId로 모든 Report 조회
        List<Report> reports = reportRepository.findAllByFacility(facility);

        // Report -> FacilityImageSummaryResponse 변환
        List<FacilityImageSummaryResponse> reportImages = reports.stream()
                .map(FacilityImageSummaryResponse::from)
                .toList();

        return FacilityImageListResponse.builder()
                .total(reportImages.size())
                .reportImages(reportImages)
                .build();
    }

    // 시설 이미지 신고
    @Transactional
    public void flagFacilityImage(String loginId, Long facilityId, FacilityImageFlagRequest request) {
        User user = entityFinder.getUserByLoginId(loginId);
        Facility facility = entityFinder.getFacilityById(facilityId);
        School school = entityFinder.getSchoolByFacility(facility);

        // 시설 이미지 열람 권한 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.REPORT_NOT_FOUND));

        // 신고 횟수 누적 3회 이상이면 자동 비공개 처리
        report.addFlag();
        if (report.getFlagCount() >= 3) { report.setIsPublic(false);  }

        ReportFlag flag = request.toEntity(report, user);
        reportFlagRepository.save(flag);
    }
}
