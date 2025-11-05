package com.efub.gogildong.schools.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.SchoolViewRequest;
import com.efub.gogildong.schools.dto.request.SchoolViewRequestRequest;
import com.efub.gogildong.schools.dto.response.SchoolViewRequestResponse;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.schools.repository.SchoolViewRequestRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolViewRequestService {

    private final SchoolRepository schoolRepository;
    private final SchoolViewRequestRepository schoolViewRequestRepository;

    // 학교 정보 열람 신청
    public SchoolViewRequestResponse createSchoolViewRequest(User user, SchoolViewRequestRequest request) {
        School school = schoolRepository.findBySchoolId(request.getSchoolId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));

        SchoolViewRequest viewRequest = request.toEntity(school, user);
        schoolViewRequestRepository.save(viewRequest);
        return SchoolViewRequestResponse.from(viewRequest);
    }
}
