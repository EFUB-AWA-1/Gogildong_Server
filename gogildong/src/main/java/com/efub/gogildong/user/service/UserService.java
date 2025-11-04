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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    // 내부인 생성
    @Transactional
    public CreateInternalUserResponseDto createInternalUser(CreateInternalUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // schoolCode로 학교 조회
        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // 내부인 생성
        User user = request.toEntity();
        user.changeSchool(school);

        User saved = userRepository.save(user);
        return CreateInternalUserResponseDto.from(saved);
    }

    // 내부인 소속 학교 변경

    // 외부인 생성
    @Transactional
    public CreateUserResponseDto createExternalUser(CreateExternalUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // 외부인 생성
        User user = request.toEntity();

        User saved = userRepository.save(user);
        return CreateUserResponseDto.from(saved);
    }

    // 학교 관리자 생성
    @Transactional
    public CreateInternalUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // schoolCode로 학교 조회
        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // adminCode로 학교 관리자 검증
        if (!school.matchesAdminCode(request.getAdminCode())) {
            throw new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND);
        }

        // 외부인 생성
        User user = request.toEntity();
        user.changeSchool(school);

        User saved = userRepository.save(user);
        return CreateInternalUserResponseDto.from(saved);
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
