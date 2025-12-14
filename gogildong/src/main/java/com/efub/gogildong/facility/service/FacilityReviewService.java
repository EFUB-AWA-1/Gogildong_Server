package com.efub.gogildong.facility.service;

import com.efub.gogildong.ai.service.FacilityReviewSummaryService;
import com.efub.gogildong.facility.domain.*;
import com.efub.gogildong.facility.dto.request.FacilityReviewRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewSummaryResponse;
import com.efub.gogildong.facility.respository.FacilityReviewFlagRepository;
import com.efub.gogildong.facility.respository.FacilityReviewLikeRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.point.service.PointService;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.shops.service.CoinService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityReviewService {

    private final FacilityReviewFlagRepository facilityReviewFlagRepository;
    private final EntityFinder entityFinder;
    private final FacilityReviewRepository facilityReviewRepository;
    private final SchoolViewRequestService schoolViewRequestService;
    private final PointService pointService;
    private final FacilityReviewLikeRepository facilityReviewLikeRepository;

    private static final int REVIEW_POINTS = 5;
    private final CoinService coinService;
    private final FacilityReviewSummaryService facilityReviewSummaryService;

    // 시설 리뷰 조회
    @Transactional(readOnly = true)
    public FacilityReviewListResponse getFacilityReviews(String loginId, Long facilityId, int page) {
        User user = entityFinder.getUserByLoginId(loginId);
        Facility facility = entityFinder.getFacilityById(facilityId);
        School school = entityFinder.getSchoolByFacility(facility);

        // 시설 리뷰 열람 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FacilityReview> reviewPage = facilityReviewRepository.findByFacility(facility, pageRequest);

        List<FacilityReview> reviews = reviewPage.getContent();

        // 유저가 좋아요 누른 리뷰 ID 목록
        List<Long> likedReviewIds =
                facilityReviewLikeRepository.findLikedReviewIdsByUserAndReviews(user, reviews);

        List<FacilityReviewSummaryResponse> reviewList = reviews.stream()
                .map(review -> FacilityReviewSummaryResponse.from(
                        review,
                        likedReviewIds.contains(review.getFacilityReviewId())
                ))
                .toList();

        return FacilityReviewListResponse.from(reviewPage, facility, reviewList);
    }

    // 시설 리뷰 작성
    @Transactional
    public FacilityReviewResponse createFacilityReview(String loginId, FacilityReviewRequest request) {
        User user = entityFinder.getUserByLoginId(loginId);
        Facility facility = entityFinder.getFacilityById(request.getFacilityId());
        School school = entityFinder.getSchoolByFacility(facility);

        // 열람 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        FacilityReview review = request.toEntity(facility, user);
        facilityReviewRepository.save(review);

        facilityReviewSummaryService.summarizeFacilityReview(facility);

        pointService.addPoints(user.getUserId(), REVIEW_POINTS);
        coinService.earnCoin(user, REVIEW_POINTS);

        return FacilityReviewResponse.from(review);
    }

    // 시설 리뷰 수정
    @Transactional
    public FacilityReviewResponse updateFacilityReview(String loginId, Long reviewId, FacilityReviewUpdateRequest request) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 열람 권한(학교가 바뀌면 이전 학교에 작성했던 리뷰에도 접근 불가) + 작성자 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);
        validateReviewAuthor(review, user);

        review.updateReviewText(request.getReviewText());
        return FacilityReviewResponse.from(review);
    }

    // 시설 리뷰 삭제
    @Transactional
    public void deleteFacilityReview(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 열람 권한(학교가 바뀌면 이전 학교에 작성했던 리뷰에도 접근 불가) + 작성자 확인
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);
        validateReviewAuthor(review, user);

        facilityReviewRepository.delete(review);
    }

    private void validateReviewAuthor(FacilityReview review, User user) {
        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }
    }

    // 리뷰 신고
    @Transactional
    public void flagFacilityReview(String loginId, Long reviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        // 중복 신고 체크
        boolean alreadyFlagged = facilityReviewFlagRepository.existsByUserAndReview(user, review);
        if (alreadyFlagged) {
            throw new GoGildongException(ExceptionCode.DUPLICATE_FLAG);
        }

        // 신고 기록 생성
        FacilityReviewFlag reviewFlag = FacilityReviewFlag.builder()
                .review(review)
                .user(user)
                .build();
        facilityReviewFlagRepository.save(reviewFlag);

        // 리뷰 신고 횟수 갱신
        review.addFlag();

        // 신고 3회 이상이면 리뷰 삭제 처리
        if (review.getFlagCount() >= 3) {
            deleteFacilityReview(loginId, reviewId);
        }
    }
}
