package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewLike;
import com.efub.gogildong.facility.respository.FacilityReviewLikeRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityReviewLikeService {

    private final FacilityReviewLikeRepository facilityReviewLikeRepository;
    private final FacilityReviewRepository facilityReviewRepository;

    private final EntityFinder entityFinder;
    private final SchoolViewRequestService schoolViewRequestService;

    // 시설 리뷰 좋아요 추가
    @Transactional
    public void createFacilityReviewLike(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // 1) Like row insert (유니크 제약으로 중복 방지)
        try {
            facilityReviewLikeRepository.save(
                    FacilityReviewLike.builder()
                            .facilityReview(review)
                            .user(user)
                            .build()
            );
        } catch (DataIntegrityViolationException e) {
            // (facility_review_id, user_id) UNIQUE 위반 시
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_ALREADY_EXISTS);
        }

        // 2) Count +1 (DB update)
        int updated = facilityReviewRepository.increaseLikeCount(reviewId);
        if (updated == 0) {
            // 이론상 review가 없으면 여기로 옴(보통 entityFinder에서 이미 방지됨)
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND);
        }
    }

    // 시설 리뷰 좋아요 취소
    @Transactional
    public void deleteFacilityReviewLike(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // 1) Like row delete
        int deleted = facilityReviewLikeRepository.deleteByReviewIdAndUserId(reviewId, user.getUserId());
        if (deleted == 0) {
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_NOT_FOUND);
        }

        // 2) Count -1 (DB update)
        int updated = facilityReviewRepository.decreaseLikeCount(reviewId);
        if (updated == 0) {
            // likeCount가 이미 0인 이상상황(정합성 깨진 경우) 방어
            // 필요하면 별도 ExceptionCode로 처리해도 됨
            throw new GoGildongException(ExceptionCode.INVALID_REQUEST);
        }
    }
}
