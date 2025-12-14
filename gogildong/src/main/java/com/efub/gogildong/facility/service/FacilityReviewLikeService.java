package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewLike;
import com.efub.gogildong.facility.dto.response.FacilityReviewLikeResponse;
import com.efub.gogildong.facility.respository.FacilityReviewLikeRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityReviewLikeService {

    private final FacilityReviewLikeRepository facilityReviewLikeRepository;
    private final EntityFinder entityFinder;
    private final SchoolViewRequestService schoolViewRequestService;

    // 시설 리뷰 좋아요 생성
    @Transactional
    public FacilityReviewLikeResponse createFacilityReviewLike(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 접근 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // 유저가 해당 리뷰에 이미 좋아요를 생성했는지 검사
        if(facilityReviewLikeRepository.existsByFacilityReviewAndUser(review, user)) {
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_ALREADY_EXISTS);
        }

        // 리뷰 상태 변결
        review.addLike();
        // 좋아요 엔티티 생성
        FacilityReviewLike like = FacilityReviewLike.builder()
                .facilityReview(review)
                .user(user)
                .build();

        facilityReviewLikeRepository.save(like);
        return FacilityReviewLikeResponse.from(like);
    }

    // 시설 리뷰 좋아요 취소
    @Transactional
    public void deleteFacilityReviewLike(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 접근 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // (reviewId, userId) 조건으로 DB에서 바로 삭제
        int deleted = facilityReviewLikeRepository.deleteByReviewIdAndUserId(reviewId, user.getUserId());
        if (deleted == 0) {
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_NOT_FOUND);
        }

        // 카운트 감소
        review.deleteLike();
    }
}
