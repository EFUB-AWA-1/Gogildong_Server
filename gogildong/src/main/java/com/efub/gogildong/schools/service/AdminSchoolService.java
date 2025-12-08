package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.JwtTokenProvider;
import com.efub.gogildong.schools.domain.EduLevel;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.dto.request.AdminAddSchoolRequest;
import com.efub.gogildong.schools.dto.response.AdminAddSchoolResponse;
import com.efub.gogildong.schools.dto.response.AdminSchoolListResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.efub.gogildong.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    // 등록된 학교 리스트 조회
    @Transactional
    public AdminSchoolListResponse getSchools(int page, int size, String accessToken) {

        // 토큰에서 role 추출
        String role = jwtTokenProvider.getUserRole(accessToken);

        // ADMIN이 아니면 예외 발생
        if (!"ADMIN".equals(role)) {
            throw new GoGildongException(ExceptionCode.ACCESS_DENIED);
        }

        // DB에서 페이징 처리
        Page<School> schoolPage = schoolRepository.findAll(PageRequest.of(page, size));

        // DTO 변환
        List<AdminSchoolListResponse.SchoolDto> schoolDtos = schoolPage.stream()
                .map(school -> {
                    // 학교 관리자 조회
                    Optional<User> admin = userRepository.findBySchoolAndRole(school, UserRole.ADMIN)
                            .stream()
                            .findFirst();

                    String adminPhone = admin.map(User::getPhone).orElse("");

                    return new AdminSchoolListResponse.SchoolDto(
                            school.getRegion(),
                            school.getEduLevel().name(),
                            school.getSchoolName(),
                            school.getSchoolCode(),
                            "REGISTERED",
                            adminPhone,
                            school.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        // total: 전체 데이터 개수, schools: 현재 페이지 데이터
        return new AdminSchoolListResponse(
                (int) schoolPage.getTotalElements(), // 전체 학교 수
                schoolDtos
        );
    }

    // 학교 검색
    @Transactional
    public AdminSchoolListResponse searchSchools(String region, String level, String keyword, int page, int size, String accessToken) {

        // 토큰에서 role 추출
        String role = jwtTokenProvider.getUserRole(accessToken);

        // ADMIN이 아니면 예외 발생
        if (!"ADMIN".equals(role)) {
            throw new GoGildongException(ExceptionCode.ACCESS_DENIED);
        }

        EduLevel eduLevel = null;
        if (level != null && !level.isBlank() && !level.equalsIgnoreCase("all")) {
            try {
                eduLevel = EduLevel.valueOf(level.toLowerCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("올바르지 않은 eduLevel 값입니다.");
            }
        }

        Page<School> schoolPage = schoolRepository.searchSchools(region, eduLevel, keyword, PageRequest.of(page, size));

        List<AdminSchoolListResponse.SchoolDto> schoolDtos = schoolPage.stream()
                .map(school -> {
                    Optional<User> admin = userRepository.findBySchoolAndRole(school, UserRole.ADMIN)
                            .stream()
                            .findFirst();

                    String adminPhone = admin.map(User::getPhone).orElse("");

                    return new AdminSchoolListResponse.SchoolDto(
                            school.getRegion(),
                            school.getEduLevel().name(),
                            school.getSchoolName(),
                            school.getSchoolCode(),
                            "REGISTERED",
                            adminPhone,
                            school.getCreatedAt()
                    );
                }).collect(Collectors.toList());

        return new AdminSchoolListResponse((int) schoolPage.getTotalElements(), schoolDtos);
    }

    // 학교 추가
    @Transactional
    public AdminAddSchoolResponse addSchool(AdminAddSchoolRequest request, String accessToken) {

        // 토큰에서 role 추출
        String role = jwtTokenProvider.getUserRole(accessToken);

        // SUPER_ADMIN이 아니면 예외 발생
        if (!"SUPER_ADMIN".equals(role)) {
            throw new GoGildongException(ExceptionCode.ACCESS_DENIED);
        }

        // eduLevel 문자열 -> Enum
        EduLevel eduLevel;
        try {
            eduLevel = EduLevel.valueOf(request.getEduLevel().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 eduLevel 값입니다.");
        }

        // Location -> Point
        double lon = request.getLocation().getCoordinates()[0];
        double lat = request.getLocation().getCoordinates()[1];
        Point location = geometryFactory.createPoint(new Coordinate(lon, lat));

        // School 엔티티 생성
        School school = School.builder()
                .schoolCode(request.getSchoolCode())
                .schoolName(request.getSchoolName())
                .address(request.getAddress())
                .location(location)
                .eduLevel(eduLevel)
                .adminCode(request.getAdminCode()) // null이면 School.prePersist에서 랜덤 생성
                .hasSpecialClass(request.getHasSpecialClass())
                .region(request.getRegion())
                .build();

        schoolRepository.save(school);

        return new AdminAddSchoolResponse(
                school.getSchoolId(),
                school.getSchoolCode(),
                school.getSchoolName(),
                school.getAddress(),
                school.getEduLevel().name(),
                school.getAdminCode(),
                school.getHasSpecialClass(),
                school.getRegion(),
                school.getCreatedAt(),
                school.getUpdatedAt()
        );
    }
}
