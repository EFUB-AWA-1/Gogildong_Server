package com.efub.gogildong.schools.service;

import com.efub.gogildong.schools.domain.EduLevel;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.dto.response.AdminSchoolListResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    // 등록된 학교 리스트 조회
    public AdminSchoolListResponse getSchools(int page, int size) {
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
    public AdminSchoolListResponse searchSchools(String region, String level, String keyword, int page, int size) {

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
}
