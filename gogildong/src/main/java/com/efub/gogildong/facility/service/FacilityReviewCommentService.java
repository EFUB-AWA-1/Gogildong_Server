package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewComment;
import com.efub.gogildong.facility.dto.request.FacilityReviewCommentRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewCommentUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentResponse;
import com.efub.gogildong.facility.respository.FacilityReviewCommentRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.point.service.PointService;
import com.efub.gogildong.schools.domain.School;
import com.efub.gogildong.schools.service.SchoolViewRequestService;
import com.efub.gogildong.shops.service.CoinService;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityReviewCommentService {

    private final FacilityReviewCommentRepository facilityReviewCommentRepository;
    private final EntityFinder entityFinder;
    private final SchoolViewRequestService schoolViewRequestService;
    private final PointService pointService;

    private static final int REVIEW_COMMENT_POINTS = 5;
    private final CoinService coinService;

    // 시설 리뷰 댓글 조회
    @Transactional(readOnly = true)
    public FacilityReviewCommentListResponse getFacilityReviewComments(String loginId, Long facilityReviewId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(facilityReviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 댓글 열람 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        List<FacilityReviewComment> commentList =
                facilityReviewCommentRepository.findAllByFacilityReview_FacilityReviewId(facilityReviewId);

        List<FacilityReviewCommentResponse> comments = commentList.stream()
                .map(FacilityReviewCommentResponse::from)
                .toList();

        return FacilityReviewCommentListResponse.from(comments);
    }

    // 시설 리뷰 댓글 작성
    @Transactional
    public FacilityReviewCommentResponse createFacilityReviewComment(String loginId, Long reviewId, FacilityReviewCommentRequest request) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 댓글 작성 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        FacilityReviewComment comment = request.toEntity(review, user);
        facilityReviewCommentRepository.save(comment);

        pointService.addPoints(user.getUserId(), REVIEW_COMMENT_POINTS);
        coinService.earnCoin(user, REVIEW_COMMENT_POINTS);

        return FacilityReviewCommentResponse.from(comment);
    }

    // 시설 리뷰 댓글 수정
    @Transactional
    public FacilityReviewCommentResponse updateFacilityReviewComment(String loginId, Long reviewId, Long commentId, FacilityReviewCommentUpdateRequest request) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 댓글 접근 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        FacilityReviewComment comment = entityFinder.getReviewCommentById(commentId);

        // 작성자 검사
        validateCommentAuthor(comment, user);

        comment.updateCommentText(request.getCommentText());
        facilityReviewCommentRepository.save(comment);
        return FacilityReviewCommentResponse.from(comment);
    }

    // 시설 리뷰 댓글 삭제
    @Transactional
    public void deleteFacilityReviewComment(String loginId, Long reviewId, Long commentId) {
        User user = entityFinder.getUserByLoginId(loginId);
        FacilityReview review = entityFinder.getReviewById(reviewId);
        School school = entityFinder.getSchoolByFacility(review.getFacility());

        // 시설 리뷰 댓글 접근 권한 검사
        schoolViewRequestService.validateViewRequestBySchoolAndUser(school, user);

        FacilityReviewComment comment = entityFinder.getReviewCommentById(commentId);

        // 작성자 검사
        validateCommentAuthor(comment, user);

        facilityReviewCommentRepository.deleteById(commentId);
    }

    // 댓글 작성자 검사
    private void validateCommentAuthor(FacilityReviewComment comment, User user) {
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new GoGildongException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }
    }

}
