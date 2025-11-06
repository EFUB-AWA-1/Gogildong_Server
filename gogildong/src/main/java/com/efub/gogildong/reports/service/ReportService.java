package com.efub.gogildong.reports.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.domain.Restroom;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.RestroomRespository;
import com.efub.gogildong.facility.service.FacilityNameGenerator;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.reports.domain.RestRoomReport;
import com.efub.gogildong.reports.dto.request.NewRestRoomReportRequest;
import com.efub.gogildong.reports.repository.ReportRepository;
import com.efub.gogildong.reports.repository.RestRoomReportRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final RestRoomReportRepository restRoomReportRepository;
    private final EntityFinder finder;
    private final FacilityRepository facilityRepository;
    private final RestroomRespository restroomRespository;

    /*
    * 시설을 생성하고 해당 시설에 대한 제보를 생성합니다.
    * */
    @Transactional
    public void createReportAboutNewFacility(String loginId, NewRestRoomReportRequest request){
        // 작성자 권한 확인
        User user = validateReportWriterAndGet(loginId, request.getFloorId());
        Floor floor = finder.getFloorById(request.getFloorId());

        // 시설 이름 생성
        String facilityName = generateFacilityName(floor);

        // 관련 엔티티 생성
        Facility facility = NewRestRoomReportRequest.toFacilityEntity(request, facilityName, floor);
        Report report = createReport();
        RestRoomReport restRoomReport = NewRestRoomReportRequest.toRestRoomReportEntity(request, report);
        Restroom newRestroom = NewRestRoomReportRequest.toRestroomEntity(request, facility, restRoomReport);

        // 연관관계 생성
        user.addReport(report);

        // 관련 엔티티 저장
        facilityRepository.save(facility);
        reportRepository.save(report);
        restRoomReportRepository.save(restRoomReport);
        restroomRespository.save(newRestroom);
    }

    /*
    * 제보를 생성합니다.
    * */
    private Report createReport(){
        return Report.builder()
                .isPublic(true)
                .reportType(FacilityType.RESTROOM)
                .build();
    }

    /*
    * 작성자가 해당 학교 소속인지 확인하고 반환합니다.
    * */
    private User validateReportWriterAndGet(String loginId, Long floorId){
        User user = finder.getUserByLoginId(loginId);
        Floor floor = finder.getFloorById(floorId);
        if(user.getSchool() != floor.getBuilding().getSchool())
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_SCHOOL_ACCESS);
        return user;
    }

    /*
    * 시설 층 별로 시설 이름을 생성합니다.
    * */
    private String generateFacilityName(Floor floor){
        Long countByFloor = facilityRepository.countByFloor(floor);
        return FacilityNameGenerator.generateFacilityName(floor.getFloorName(), countByFloor + 1);
    }
}
