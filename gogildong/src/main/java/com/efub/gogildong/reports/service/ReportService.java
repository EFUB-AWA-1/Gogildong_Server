package com.efub.gogildong.reports.service;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.RestroomRespository;
import com.efub.gogildong.facility.service.FacilityNameGenerator;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.point.service.PointService;
import com.efub.gogildong.reports.domain.*;
import com.efub.gogildong.reports.dto.request.*;
import com.efub.gogildong.reports.dto.request.classroom.ClassroomReportRequest;
import com.efub.gogildong.reports.dto.request.classroom.NewClassroomReportRequest;
import com.efub.gogildong.reports.dto.request.elevator.ElevatorReportRequest;
import com.efub.gogildong.reports.dto.request.elevator.NewElevatorReportRequest;
import com.efub.gogildong.reports.dto.request.restroom.NewRestRoomReportRequest;
import com.efub.gogildong.reports.dto.request.restroom.RestRoomReportRequest;
import com.efub.gogildong.reports.dto.response.*;
import com.efub.gogildong.reports.dto.response.classroom.ClassroomAggregateStat;
import com.efub.gogildong.reports.dto.response.elevator.ElevatorAggregateStat;
import com.efub.gogildong.reports.dto.response.restroom.RestRoomAggregateStat;
import com.efub.gogildong.reports.dto.response.restroom.RestRoomReportResponse;
import com.efub.gogildong.reports.dto.summary.ReportFlagSummary;
import com.efub.gogildong.reports.dto.summary.ReportSummary;
import com.efub.gogildong.reports.repository.*;
import com.efub.gogildong.shops.service.CoinService;
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
    private final ReportFlagRepository reportFlagRepository;
    private final ElevatorReportRepository elevatorReportRepository;
    private final ClassroomReportRepository classroomReportRepository;
    private final CoinService coinService;
    private final PointService pointService;
    private final int REPORT_POINT = 20;
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
        Facility savedFacility = facilityRepository.save(facility);
        Report report = createRestroomReport(savedFacility, user);
        Report savedReport = reportRepository.save(report);
        RestRoomReport restRoomReport = NewRestRoomReportRequest.toRestRoomReportEntity(request, savedReport);
        Restroom newRestroom = NewRestRoomReportRequest.toRestroomEntity(request, facility, restRoomReport);

        // 연관관계 생성
        user.addReport(report);
        restRoomReport.setReport(report);
        restRoomReport.setRestroom(newRestroom);
        facility.setRestroom(newRestroom);

        // 관련 엔티티 저장
        reportRepository.save(report);
        facilityRepository.save(facility);

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
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
        Report report = createRestroomReport(facility, user);
        Report savedReport = reportRepository.save(report);
        RestRoomReport restRoomReport = NewRestRoomReportRequest.toRestRoomReportEntity(request, savedReport);
        restRoomReport.setReport(report);
        facility.getRestroom().addRestRoomReport(restRoomReport);

        user.addReport(report);

        facility.updateNickname(request.getFacilityName());
        restRoomReportRepository.save(restRoomReport);

        updateRestroomAggregate(facility.getRestroom());

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
    }

    /*
    * 화장실 제보를 생성합니다.
    * */
    private Report createRestroomReport(Facility facility, User user) {
        return Report.builder()
                .isPublic(true)
                .status(ReportStatus.PENDING)
                .facility(facility)
                .user(user)
                .reportType(FacilityType.RESTROOM)
                .build();
    }

    /*
     * 엘리베이터를 생성하고 해당 시설에 대한 제보를 생성합니다.
     * */
    @Transactional
    public void createReportAboutNewElevator(String loginId, NewElevatorReportRequest request){
        // 작성자 권한 확인
        User user = validateReportWriterByFloorAndGet(loginId, request.getFloorId());
        Floor floor = finder.getFloorById(request.getFloorId());

        // 시설 이름 생성
        String facilityName = generateFacilityName(floor);

        // 관련 엔티티 생성
        Facility facility = NewElevatorReportRequest.toFacilityEntity(request, facilityName, floor);
        Facility savedFacility = facilityRepository.save(facility);
        Report report = Report.builder()
                .isPublic(true)
                .reportType(FacilityType.ELEVATOR)
                .status(ReportStatus.PENDING)
                .facility(savedFacility)
                .user(user)
                .build();
        Report savedReport = reportRepository.save(report);


        ElevatorReport elevatorReport = NewElevatorReportRequest.toElevatorReportEntity(request, report);
        Elevator newElevator = NewElevatorReportRequest.toElevatorEntity(request, facility, elevatorReport);

        // 연관관계 생성
        user.addReport(report);
        elevatorReport.setReport(savedReport);
        elevatorReport.setElevator(newElevator);
        facility.setElevator(newElevator);

        // 관련 엔티티 저장
        reportRepository.save(report);
        facilityRepository.save(facility);

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
    }

    /*
     * 기존 엘리베이터에 대한 제보를 생성합니다.
     * */
    @Transactional
    public void createReportAboutExistingElevator(String loginId, ElevatorReportRequest request){
        // 작성자 권한 확인
        Facility facility = finder.getFacilityById(request.getFacilityId());
        User user = validateReportWriterByFloorAndGet(loginId, facility.getFloor().getFloorId());

        // 관련 엔티티 생성
        Report report = Report.builder()
                .isPublic(true)
                .reportType(FacilityType.ELEVATOR)
                .facility(facility)
                .status(ReportStatus.PENDING)
                .user(user)
                .build();
        ElevatorReport elevatorReport = NewElevatorReportRequest.toElevatorReportEntity(request, report);
        elevatorReport.setReport(report);
        facility.getElevator().addElevatorReport(elevatorReport);

        user.addReport(report);

        facility.updateNickname(request.getFacilityName());

        reportRepository.save(report);
        elevatorReportRepository.save(elevatorReport);

        updateElevatorAggregate(facility.getElevator());

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
    }

    /*
    새 교실에 대한 제보를 생성합니다.
     */
    @Transactional
    public void createReportAboutNewClassroom(String loginId, NewClassroomReportRequest request){
        // 작성자 권한 확인
        User user = validateReportWriterByFloorAndGet(loginId, request.getFloorId());
        Floor floor = finder.getFloorById(request.getFloorId());

        // 시설 이름 생성
        String facilityName = generateFacilityName(floor);

        // 관련 엔티티 생성
        Facility facility = NewClassroomReportRequest.toFacilityEntity(request, facilityName, floor);
        Facility savedFacility = facilityRepository.save(facility);
        Report report = Report.builder()
                .isPublic(true)
                .reportType(FacilityType.CLASSROOM)
                .facility(savedFacility)
                .status(ReportStatus.PENDING)
                .user(user)
                .build();
        Report savedReport = reportRepository.save(report);

        ClassroomReport classroomReport = NewClassroomReportRequest.toClassroomReportEntity(request, report);
        Classroom newClassroom = NewClassroomReportRequest.toClassroomEntity(request, facility, classroomReport);

        // 연관관계 생성
        user.addReport(report);
        classroomReport.setReport(savedReport);
        classroomReport.setClassroom(newClassroom);
        facility.setClassroom(newClassroom);

        // 관련 엔티티 저장
        reportRepository.save(report);
        facilityRepository.save(facility);

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
    }

    /*
     * 기존 교실에 대한 제보를 생성합니다.
     * */
    @Transactional
    public void createReportAboutExistingClassroom(String loginId, ClassroomReportRequest request){
        // 작성자 권한 확인
        Facility facility = finder.getFacilityById(request.getFacilityId());
        User user = validateReportWriterByFloorAndGet(loginId, facility.getFloor().getFloorId());

        // 관련 엔티티 생성
        Report report = Report.builder()
                .isPublic(true)
                .reportType(FacilityType.CLASSROOM)
                .facility(facility)
                .status(ReportStatus.PENDING)
                .user(user)
                .build();
        ClassroomReport classroomReport = NewClassroomReportRequest.toClassroomReportEntity(request, report);
        classroomReport.setReport(report);
        facility.getClassroom().addClassroomReport(classroomReport);

        user.addReport(report);

        facility.updateNickname(request.getFacilityName());

        reportRepository.save(report);
        classroomReportRepository.save(classroomReport);

        updateClassroomAggregate(facility.getClassroom());

        // 제보 시 포인트와 엽전 획득
        getPointAndCoinByReport(user);
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

    /*
     * 최근 데이터를 기반으로 엘리베이터 정보를 재구성합니다.
     * */
    private void updateElevatorAggregate(Elevator elevator){
        List<ElevatorReport> reports = elevatorReportRepository.findPublicByElevator(elevator);

        // 숫자 평균, min, max
        Float avgDoorWidth = (float) reports.stream().mapToDouble(ElevatorReport::getDoorWidth).average().orElse(elevator.getDoorWidth());
        Float avgDoorHeight = (float) reports.stream().mapToDouble(ElevatorReport::getDoorHeight).average().orElse(elevator.getDoorHeight());
        Float avgMaxControlPanelHeight = (float) reports.stream().mapToDouble(ElevatorReport::getMaxControlPanelHeight).average().orElse(elevator.getMaxControlPanelHeight());
        Float minDoorWidth = (float) reports.stream().mapToDouble(ElevatorReport::getDoorWidth).min().orElse(elevator.getMinDoorWidth());
        Float maxDoorWidth = (float) reports.stream().mapToDouble(ElevatorReport::getDoorWidth).max().orElse(elevator.getMaxDoorWidth());

        ElevatorAggregateStat stat = ElevatorAggregateStat.builder()
                .avgDoorWidth(avgDoorWidth)
                .avgDoorHeight(avgDoorHeight)
                .avgMaxControlPanelHeight(avgMaxControlPanelHeight)
                .minDoorWidth(minDoorWidth)
                .maxDoorWidth(maxDoorWidth)
                .build();

        // Elevator update
        elevator.updateAggregate(stat);
    }

    /*
     * 최근 데이터를 기반으로 교실 정보를 재구성합니다.
     * */
    private void updateClassroomAggregate(Classroom classroom){
        List<ClassroomReport> reports = classroomReportRepository.findPublicByClassroom(classroom);

        // boolean = 비율
        float avgHasThreshold = reports.stream().filter(ClassroomReport::getHasThreshold).count() / (float) reports.size();

        // 숫자 평균, min, max
        Float avgDoorWidth = (float) reports.stream().mapToDouble(ClassroomReport::getDoorWidth).average().orElse(classroom.getDoorWidth());
        Float avgDoorHeight = (float) reports.stream().mapToDouble(ClassroomReport::getDoorHeight).average().orElse(classroom.getDoorHeight());
        Float avgMinAisleWidth = (float) reports.stream().mapToDouble(ClassroomReport::getMinAisleWidth).average().orElse(classroom.getMinAisleWidth());
        Float minDoorWidth = (float) reports.stream().mapToDouble(ClassroomReport::getDoorWidth).min().orElse(classroom.getMinDoorWidth());
        Float maxDoorWidth = (float) reports.stream().mapToDouble(ClassroomReport::getDoorWidth).max().orElse(classroom.getMaxDoorWidth());

        ClassroomAggregateStat stat = ClassroomAggregateStat.builder()
                .avgDoorWidth(avgDoorWidth)
                .avgDoorHeight(avgDoorHeight)
                .avgMinAisleWidth(avgMinAisleWidth)
                .avgHasThreshold(avgHasThreshold)
                .minDoorWidth(minDoorWidth)
                .maxDoorWidth(maxDoorWidth)
                .build();

        // Classroom update
        classroom.updateAggregate(stat);
    }

    /*
    화장실 제보 조회 (학교 관리자)
     */
    @Transactional(readOnly = true)
    public RestRoomReportResponse getRestRoomReport(Long restRoomReportId) {
        RestRoomReport restRoomReport = restRoomReportRepository.findById(restRoomReportId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.RESTROOMREPORT_NOT_FOUND));
        return RestRoomReportResponse.from(restRoomReport);
    }

    /*
    제보 목록 조회 (학교 관리자)
     */
    @Transactional(readOnly = true)
    public ReportListResponse getAllReports() {
        List<ReportSummary> reportSummaries = reportRepository.findByOrderByCreatedAtDesc().stream()
                .map(ReportSummary::from).toList();
        return new ReportListResponse(reportSummaries);
    }

    /*
    제보 공개 여부 수정 (학교 관리자)
     */
    @Transactional
    public ReportSummary updateReportPublicStatus(Long reportId, UpdateReportPublicStatusRequest requestDto) {

        Report report = reportRepository.findByReportId(reportId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.REPORT_NOT_FOUND));

        report.setIsPublic(requestDto.getIsPublic());
        return ReportSummary.from(report);
    }

    /*
    이미지 제보 신고 조회 (학교 관리자)
     */
    public ReportFlagListResponse findReportFlagsByReportId(Long reportId) {

        List<ReportFlag> reportFlags = reportFlagRepository.findAllByReport_ReportId(reportId);

        List<ReportFlagSummary> flags = reportFlags.stream()
                .map(ReportFlagSummary::of)
                .collect(Collectors.toList());

        return ReportFlagListResponse.of(reportId, flags);
    }

    /*
     * 제보 시 20 포인트 지급, 20 엽전 지급
     * */
    private void getPointAndCoinByReport(User user) {
        coinService.earnCoin(user, REPORT_POINT);
        pointService.addPoints(user.getUserId(), REPORT_POINT);
    }
}
