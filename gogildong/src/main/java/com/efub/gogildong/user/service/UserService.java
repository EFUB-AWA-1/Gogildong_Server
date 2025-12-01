package com.efub.gogildong.user.service;

import com.efub.gogildong.email.service.EmailVerificationService;
import com.efub.gogildong.global.exception.BusinessValidationException;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.shops.service.UserItemService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.dto.common.ErrorDetailDto;
import com.efub.gogildong.user.dto.request.CreateAdminUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateExternalUserRequestDto;
import com.efub.gogildong.user.dto.request.CreateInternalUserRequestDto;
import com.efub.gogildong.user.dto.request.UpdateUserRequestDto;
import com.efub.gogildong.user.dto.response.CreateUserResponseDto;
import com.efub.gogildong.user.dto.response.InternalUserResponseDto;
import com.efub.gogildong.user.dto.response.UpdateUserResponseDto;
import com.efub.gogildong.user.dto.response.UserResponseDto;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserItemService userItemService;
    private final EmailVerificationService emailVerificationService;

    /* ====================== 회원가입 ====================== */

    // 내부인 생성
    @Transactional
    public InternalUserResponseDto createInternalUser(CreateInternalUserRequestDto request) {
        List<ErrorDetailDto> errors = new ArrayList<>();

        // 아이디 중복 검증
        validateLoginIdDuplicate(request.getLoginId(), errors);
        // 이메일 + 학교코드 관련 검증 (공통/내부인 전용)
        School school = validateInternalSignup(request.getEmail(), request.getSchoolCode(), errors);
        // 에러가 하나라도 있으면 한 번에 응답
        throwIfHasErrors(errors);

        // ==== 검증 통과 시에만 실행되는 "정상 로직" ====
        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.changeSchool(school);

        User savedUser = userRepository.save(user);
        userItemService.wearDefaultItemsForUser(savedUser);

        return InternalUserResponseDto.from(savedUser);
    }

    // 외부인 생성
    @Transactional
    public CreateUserResponseDto createExternalUser(CreateExternalUserRequestDto request) {
        List<ErrorDetailDto> errors = new ArrayList<>();

        // 아이디 중복 검증
        validateLoginIdDuplicate(request.getLoginId(), errors);
        // 이메일 형식 + 중복
        validateCommonSignup(request.getEmail(), errors);
        // 이메일 인증 여부
        validateEmailVerified(request.getEmail(), errors);

        throwIfHasErrors(errors);

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        userItemService.wearDefaultItemsForUser(savedUser);

        return CreateUserResponseDto.from(savedUser);
    }

    // 학교 관리자 생성
    @Transactional
    public InternalUserResponseDto createAdminUser(CreateAdminUserRequestDto request) {
        List<ErrorDetailDto> errors = new ArrayList<>();

        // 아이디 중복 검증
        validateLoginIdDuplicate(request.getLoginId(), errors);
        // 이메일 관련 공통 검증
        validateCommonSignup(request.getEmail(), errors);
        validateEmailVerified(request.getEmail(), errors);

        // 학교 + Admin 코드 검증
        School school = validateSchoolWithAdminCode(
                request.getSchoolCode(),
                request.getAdminCode(),
                errors
        );

        throwIfHasErrors(errors);

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.changeSchool(school);

        return InternalUserResponseDto.from(userRepository.save(user));
    }

    /* ====================== 검증 헬퍼 ====================== */

    // 내부인 회원가입 전용 검증: 이메일 + 학교코드
    private School validateInternalSignup(String email, String schoolCode, List<ErrorDetailDto> errors) {
        validateCommonSignup(email, errors);
        validateEmailVerified(email, errors);
        return validateSchoolByCode(schoolCode, errors);
    }

    // 이메일 형식 + 중복 (공통)
    private void validateCommonSignup(String email, List<ErrorDetailDto> errors) {
        // 이메일 형식
        if (!EmailValidator.isValid(email)) {
            errors.add(new ErrorDetailDto(
                    "email",
                    "유효하지 않은 이메일 형식입니다.",
                    "INVALID_EMAIL_FORMAT"
            ));
        }

        // 이메일 중복
        if (userRepository.existsByEmail(email)) {
            errors.add(new ErrorDetailDto(
                    "email",
                    ExceptionCode.EMAIL_ALREADY_EXISTS.getMessage(),
                    ExceptionCode.EMAIL_ALREADY_EXISTS.name()
            ));
        }
    }

    // loginId 중복
    private void validateLoginIdDuplicate(String loginId, List<ErrorDetailDto> errors) {
        if (userRepository.existsByLoginId(loginId)) {
            errors.add(new ErrorDetailDto(
                    "loginId",
                    ExceptionCode.DUPLICATE_LOGIN_ID.getMessage(),
                    ExceptionCode.DUPLICATE_LOGIN_ID.name()
            ));
        }
    }


    // 이메일 인증 여부
    private void validateEmailVerified(String email, List<ErrorDetailDto> errors) {
        try {
            emailVerificationService.ensureVerified(email);
        } catch (GoGildongException ex) {
            String codeName = ex.getExceptionCodeName();
            errors.add(new ErrorDetailDto(
                    "email",
                    ex.getMessage(),
                    codeName
            ));
        }
    }

    // schoolCode 유효성만 체크해서 School 반환
    private School validateSchoolByCode(String schoolCode, List<ErrorDetailDto> errors) {
        return schoolRepository.findBySchoolCode(schoolCode)
                .orElseGet(() -> {
                    errors.add(new ErrorDetailDto(
                            "schoolCode",
                            ExceptionCode.SCHOOL_BOOKMARK_NOT_FOUND.getMessage(),
                            ExceptionCode.SCHOOL_NOT_FOUND.name()
                    ));
                    return null; // 에러가 있으면 throwIfHasErrors에서 막힘
                });
    }

    // 학교 + Admin 코드 검증
    private School validateSchoolWithAdminCode(String schoolCode, String adminCode, List<ErrorDetailDto> errors) {
        School school = validateSchoolByCode(schoolCode, errors);
        if (school != null && !school.matchesAdminCode(adminCode)) {
            errors.add(new ErrorDetailDto(
                    "adminCode",
                    "유효하지 않은 Admin 코드입니다.",
                    "INVALID_ADMIN_CODE"
            ));
        }
        return school;
    }

    // 에러가 하나라도 있으면 BusinessValidationException 발생
    private void throwIfHasErrors(List<ErrorDetailDto> errors) {
        if (!errors.isEmpty()) {
            throw new BusinessValidationException(errors);
        }
    }

    /* ====================== 기타 기존 기능 ====================== */

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

    // loginId로 유저 반환
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
    }

    /* ====================== 이메일 검증 유틸 ====================== */

    public static final class EmailValidator {
        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        private EmailValidator() {}

        public static boolean isValid(String email) {
            return email != null && EMAIL_PATTERN.matcher(email).matches();
        }
    }
}
