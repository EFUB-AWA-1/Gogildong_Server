package com.efub.gogildong.user.service;

import com.efub.gogildong.shops.service.UserItemService;
import com.efub.gogildong.email.service.EmailVerificationService;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.dto.request.CreateAdminUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateExternalUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateInternalUserRequestDto;
import com.efub.gogildong.user.dto.request.UpdateUserRequestDto;
import com.efub.gogildong.user.dto.response.InternalUserResponseDto;
import com.efub.gogildong.user.dto.response.CreateUserResponseDto;
import com.efub.gogildong.user.dto.response.UpdateUserResponseDto;
import com.efub.gogildong.user.dto.response.UserResponseDto;
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
    private final UserItemService userItemService;
    private final EmailVerificationService emailVerificationService;

    // 내부인 생성
    @Transactional
    public InternalUserResponseDto createInternalUser(CreateInternalUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // 이메일 중복 체크
        validateEmailNotDuplicate(request.getEmail());

        // 이메일 인증번호 검증
        emailVerificationService.ensureVerified(request.getEmail());

        // schoolCode로 학교 조회
        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // 엔티티 생성
        User user = request.toEntity();

        // 비밀번호 인코딩
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 학교 매핑
        user.changeSchool(school);

        // 유저 캐릭터에 기본 아이템 장착
        User savedUser = userRepository.save(user);
        userItemService.wearDefaultItemsForUser(savedUser);

        return InternalUserResponseDto.from(savedUser);
    }

    // 내부인 학교 변경
    @Transactional
    public InternalUserResponseDto updateInternalUserSchoolByLoginId(String loginId, String schoolCode) {
        User user = getUserByLoginId(loginId);

        if (!user.getRole().isInternal()) {
            throw new GoGildongException(ExceptionCode.ACCESS_DENIED);
        }

        School school = schoolRepository.findBySchoolCode(schoolCode)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        user.changeSchool(school); // 변경감지로 업데이트
        return InternalUserResponseDto.from(user);
    }


    // 외부인 생성
    @Transactional
    public CreateUserResponseDto createExternalUser(CreateExternalUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // 이메일 중복 체크
        validateEmailNotDuplicate(request.getEmail());

        // 이메일 인증번호 검증
        emailVerificationService.ensureVerified(request.getEmail());

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 유저 캐릭터에 기본 아이템 장착
        User savedUser = userRepository.save(user);
        userItemService.wearDefaultItemsForUser(savedUser);

        return CreateUserResponseDto.from(savedUser);
    }

    // 학교 관리자 생성
    public InternalUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {

        // 이메일 형식 체크
        EmailValidator.validateOrThrow(request.getEmail());

        // 이메일 중복 체크
        validateEmailNotDuplicate(request.getEmail());

        // 이메일 인증번호 검증
        emailVerificationService.ensureVerified(request.getEmail());

        School school = schoolRepository.findBySchoolCode(request.getSchoolCode())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // adminCode 검증
        if (!school.matchesAdminCode(request.getAdminCode())) {
            throw new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND);
        }

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.changeSchool(school);

        return InternalUserResponseDto.from(userRepository.save(user));
    }

    // 전체 관리자 생성

    // user 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(String loginId) {
        User user = getUserByLoginId(loginId);
        return UserResponseDto.userInfo(user);
    }

    // user 정보 수정
    @Transactional
    public UpdateUserResponseDto updateUserByLoginId(String loginId, UpdateUserRequestDto requestDto) {
        User user = getUserByLoginId(loginId);

        user.updateUser(requestDto.getUsername(), requestDto.getEmail(), requestDto.getPhone());
        return UpdateUserResponseDto.from(user);
    }


    // user 삭제
    @Transactional
    public void deleteUserWithPassword(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        // bcrypt 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GoGildongException(ExceptionCode.INVALID_PASSWORD);
        }

        userRepository.delete(user); // 하드 삭제 (소프트 삭제면 user.markDeleted() 등으로 변경)
    }

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

    private void validateEmailNotDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GoGildongException(ExceptionCode.EMAIL_ALREADY_EXISTS);
        }
    }

        // login id로 유저 반환
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
    }
}
