package com.efub.gogildong.user.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.dto.request.CreateAdminUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateExternalUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateInternalUserRequestDto;
import com.efub.gogildong.user.dto.response.CreateInternalUserResponseDto;
import com.efub.gogildong.user.dto.response.CreateUserResponseDto;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    // 내부인 생성
    @Transactional
    public CreateInternalUserResponseDto createInternalUser(CreateInternalUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // schoolCode로 학교 조회
        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // 엔티티 생성
        User user = request.toEntity();

        // 비밀번호 인코딩
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 학교 매핑
        user.changeSchool(school);

        return CreateInternalUserResponseDto.from(userRepository.save(user));
    }

    // 내부인 소속 학교 변경

    // 외부인 생성
    @Transactional
    public CreateUserResponseDto createExternalUser(CreateExternalUserRequestDto request) {

        EmailValidator.validateOrThrow(request.getEmail());

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return CreateUserResponseDto.from(userRepository.save(user));
    }

    // 학교 관리자 생성
    public CreateInternalUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {

        EmailValidator.validateOrThrow(request.getEmail());

        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // adminCode 검증
        if (!school.matchesAdminCode(request.getAdminCode())) {
            throw new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND);
        }

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.changeSchool(school);

        return CreateInternalUserResponseDto.from(userRepository.save(user));
    }

    // 전체 관리자 생성

    // user 정보 수정

    // user 삭제

    // 이메일 검증
    public final class EmailValidator {
        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        private EmailValidator() {}

        public static void validateOrThrow(String email) {
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다: " + email);
            }
        }
    }
}
