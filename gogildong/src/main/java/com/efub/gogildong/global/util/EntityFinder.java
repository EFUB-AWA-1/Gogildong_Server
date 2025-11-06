package com.efub.gogildong.global.util;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewComment;
import com.efub.gogildong.facility.domain.Floor;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.FacilityReviewCommentRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.facility.respository.FloorRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final SchoolRepository schoolRepository;
    private final FacilityReviewRepository facilityReviewRepository;
    private final FacilityReviewCommentRepository facilityReviewCommentRepository;
    private final FloorRepository floorRepository;

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
    }

    public Facility getFacilityById(Long facilityId) {
        return facilityRepository.findByFacilityId(facilityId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));
    }

    public School getSchoolByFacility(Facility facility) {
        return schoolRepository.findBySchoolId(
                        facility.getFloor().getBuilding().getSchool().getSchoolId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));
    }

    public FacilityReview getReviewById(Long reviewId) {
        return facilityReviewRepository.findByFacilityReviewId(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND));
    }

    public FacilityReviewComment getReviewCommentById(Long reviewId) {
        return facilityReviewCommentRepository.findById(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_COMMENT_NOT_FOUND));
    }

    public Floor getFloorById(Long floorId) {
        return floorRepository.findByFloorId(floorId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND));
    }
}
