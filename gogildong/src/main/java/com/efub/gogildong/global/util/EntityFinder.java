package com.efub.gogildong.global.util;

import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.facility.respository.*;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.domain.constants.TagCategory;
import com.efub.gogildong.schools.repository.SchoolRepository;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final SchoolRepository schoolRepository;
    private final FacilityReviewRepository facilityReviewRepository;
    private final FacilityReviewCommentRepository facilityReviewCommentRepository;
    private final FloorRepository floorRepository;
    private final BuildingRepository buildingRepository;

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.USER_NOT_FOUND));
    }

    public Facility getFacilityById(Long facilityId) {
        return facilityRepository.findByFacilityId(facilityId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));
    }

    public School getSchoolById(Long schoolId) {
        return schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));
    }

    public School getSchoolByFacility(Facility facility) {
        return schoolRepository.findBySchoolId(
                        facility.getFloor().getBuilding().getSchool().getSchoolId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SCHOOL_NOT_FOUND));
    }

    public Building getBuildingByFloor(Floor floor) {
        return buildingRepository
                .findByFloor(floor).orElseThrow(()-> new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND));
    }

    public List<Building> getBuildingBySchool(School school){
        return buildingRepository.findBySchool(school);
    }

    public List<Facility> getAllFacilityByFloorAndType(Floor floor, TagCategory tagCategory){
        return facilityRepository.findAllByFloorAndType(floor, tagCategory.name());
    }

    public FacilityReview getReviewById(Long reviewId) {
        return facilityReviewRepository.findByFacilityReviewId(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND));
    }

    public FacilityReviewComment getReviewCommentById(Long commentId) {
        return facilityReviewCommentRepository.findByFacilityReviewCommentId(commentId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_COMMENT_NOT_FOUND));
    }

    public Floor getFloorById(Long floorId) {
        return floorRepository.findByFloorId(floorId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FLOOR_NOT_FOUND));
    }

    public Building getBuildingById(Long buildingId) {
        return buildingRepository.findByBuildingId(buildingId).orElseThrow(()-> new GoGildongException(ExceptionCode.BUILDING_NOT_FOUND));
    }
}
