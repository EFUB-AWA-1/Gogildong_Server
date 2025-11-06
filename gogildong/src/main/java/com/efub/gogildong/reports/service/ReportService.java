package com.efub.gogildong.reports.service;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.RestroomRespository;
import com.efub.gogildong.facility.service.FacilityNameGenerator;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.reports.domain.Report;
import com.efub.gogildong.reports.domain.RestRoomReport;
import com.efub.gogildong.reports.dto.request.NewRestRoomReportRequest;
import com.efub.gogildong.reports.dto.request.RestRoomReportRequest;
import com.efub.gogildong.reports.dto.response.RestRoomAggregateStat;
import com.efub.gogildong.reports.repository.ReportRepository;
import com.efub.gogildong.reports.repository.RestRoomReportRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void createReportAboutNewRestroom(String loginId, NewRestRoomReportRequest request){
        // 작성자 권한 확인
        User user = validateReportWriterByFloorAndGet(loginId, request.getFloorId());
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
    * 기존 화장실에 대한 제보를 생성합니다.
    * */
    @Transactional
    public void createReportAboutExistingRestroom(String loginId, RestRoomReportRequest request){
        // 작성자 권한 확인
        Facility facility = finder.getFacilityById(request.getFacilityId());
        User user = validateReportWriterByFloorAndGet(loginId, facility.getFloor().getFloorId());

        // 관련 엔티티 생성
        Report report = createReport();
        RestRoomReport restRoomReport = NewRestRoomReportRequest.toRestRoomReportEntity(request, report);
        facility.getRestroom().addRestRoomReport(restRoomReport);

        user.addReport(report);

        facility.updateNickname(request.getFacilityName());

        reportRepository.save(report);
        restRoomReportRepository.save(restRoomReport);

        updateRestroomAggregate(facility.getRestroom());
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
    * 층을 통해 작성자가 해당 학교 소속인지 확인하고 반환합니다.
    * */
    private User validateReportWriterByFloorAndGet(String loginId, Long floorId){
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

    /*
    * 최근 데이터를 기반으로 화장실 정보를 재구성합니다.
    * */
    private void updateRestroomAggregate(Restroom restroom){
        List<RestRoomReport> reports = restRoomReportRepository.findPublicByRestroom(restroom);

        // 1) boolean = 비율
        float avgIsAccessible = reports.stream().filter(RestRoomReport::getIsAccessible).count() / (float) reports.size();
        float avgGrabBar = reports.stream().filter(RestRoomReport::getGrabBar).count() / (float) reports.size();

        // 2) 숫자 평균, min, max
        Float avgDoorWidth = (float) reports.stream().mapToDouble(RestRoomReport::getDoorWidth).average().orElse(restroom.getDoorWidth());
        Float avgDoorHeight = (float) reports.stream().mapToDouble(RestRoomReport::getDoorHeight).average().orElse(restroom.getDoorHeight());
        Float avgToiletHeight = (float) reports.stream().mapToDouble(RestRoomReport::getToiletHeight).average().orElse(restroom.getToiletHeight());
        Float minDoorWidth = (float) reports.stream().mapToDouble(RestRoomReport::getDoorWidth).min().orElse(restroom.getMinDoorWidth());
        Float maxDoorWidth = (float) reports.stream().mapToDouble(RestRoomReport::getDoorWidth).max().orElse(restroom.getMaxDoorWidth());

        // 3) gender majority
        GenderType majorityGender = reports.stream()
                .collect(Collectors.groupingBy(RestRoomReport::getGender, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(restroom.getGender());

        RestRoomAggregateStat stat = RestRoomAggregateStat.builder()
                .majorityGender(majorityGender)
                .avgDoorWidth(avgDoorWidth)
                .avgDoorHeight(avgDoorHeight)
                .avgToiletHeight(avgToiletHeight)
                .avgGrabBar(avgGrabBar)
                .avgIsAccessible(avgIsAccessible)
                .minDoorWidth(minDoorWidth)
                .maxDoorWidth(maxDoorWidth)
                .build();

        // 4) Restroom update
        restroom.updateAggregate(stat);
    }
}
