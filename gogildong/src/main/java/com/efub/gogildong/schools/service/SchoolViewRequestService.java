package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.schools.dto.request.SchoolViewRequestRequest;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.schools.repository.SchoolViewRequestRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolViewRequestService {

    private final SchoolRepository schoolRepository;
    private final SchoolViewRequestRepository schoolViewRequestRepository;
    private final UserRepository userRepository;

    // 학교 정보 열람 신청
    public SchoolViewRequestResponse createSchoolViewRequest(String loginId, SchoolViewRequestRequest request) {
       User user = userRepository.findByLoginId(loginId)
               .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));

        School school = schoolRepository.findBySchoolId(request.getSchoolId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        // 기존 신청 여부 확인
        SchoolViewRequest existingRequest = schoolViewRequestRepository.findBySchoolAndUser(school, user).orElse(null);

        if (existingRequest != null) {
            // 이미 승인된 경우
            if (existingRequest.getStatus() == RequestStatus.APPROVED) {
                throw new GoGildongException(ExceptionCode.SCHOOL_VIEW_ALREADY_APPROVED);
            }
            // 승인 대기 중인 경우
            if (existingRequest.getStatus() == RequestStatus.PENDING) {
                throw new GoGildongException(ExceptionCode.SCHOOL_VIEW_REQUEST_PENDING);
            }
            // 거절된 경우는 재신청 허용
        }

        // 실제 소속 학교인 경우 신청 불필요
        if (user.getSchool().equals(school)) {
            throw new GoGildongException(ExceptionCode.SCHOOL_VIEW_ALREADY_APPROVED);
        }

        // 새로운 신청 생성
        SchoolViewRequest viewRequest = request.toEntity(school, user);
        schoolViewRequestRepository.save(viewRequest);
        return SchoolViewRequestResponse.from(viewRequest);
    }

    // 해당 학교, 사용자에게 열람 권한이 있는지 확인합니다.
    public void validateViewRequestBySchoolAndUser(School school, User user) {
        SchoolViewRequest schoolViewRequest =
                schoolViewRequestRepository.findBySchoolAndUser(school, user)
                        .orElse(null);

        boolean isSameSchool = user.getSchool().equals(school);
        boolean isApproved = (schoolViewRequest != null && schoolViewRequest.getStatus() == RequestStatus.APPROVED);

        // 해당 학교 소속이 아니면서, 열람 승인도 받지 않은 경우
        if (!isSameSchool && !isApproved) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_SCHOOL_ACCESS);
        }
    }
}
