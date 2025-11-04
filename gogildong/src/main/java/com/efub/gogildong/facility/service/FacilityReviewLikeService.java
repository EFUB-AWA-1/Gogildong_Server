package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewLike;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewLikeResponse;
import com.efub.gogildong.facility.respository.FacilityReviewLikeRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityReviewLikeService {

    private final FacilityReviewLikeRepository facilityReviewLikeRepository;
    private final FacilityReviewRepository facilityReviewRepository;

    // 시설 리뷰 좋아요 생성
    @Transactional
    public FacilityReviewLikeResponse createFacilityReviewLike(Long reviewId, User user) {
        FacilityReview review = facilityReviewRepository.findByFacilityReviewId(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND));

        // 유저가 해당 리뷰에 이미 좋아요를 생성했는지 검사
        if(facilityReviewLikeRepository.existsByFacilityReviewAndUser(review, user)) {
            throw new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_ALREADY_EXISTS);
        }

        FacilityReviewLike like = FacilityReviewLike.builder()
                .facilityReview(review)
                .user(user)
                .build();

        facilityReviewLikeRepository.save(like);
        return FacilityReviewLikeResponse.from(like);
    }

    // 시설 리뷰 좋아요 취소
    @Transactional
    public void deleteFacilityReviewLike(Long likeId) {
        FacilityReviewLike reviewLike = facilityReviewLikeRepository.findByFacilityReviewLikeId(likeId)
                        .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_LIKE_NOT_FOUND));
        facilityReviewLikeRepository.delete(reviewLike);
    }
}
