package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.domain.FacilityReviewComment;
import com.efub.gogildong.facility.dto.request.FacilityReviewCommentRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewCommentUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentResponse;
import com.efub.gogildong.facility.respository.FacilityReviewCommentRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityReviewCommentService {

    private final FacilityReviewCommentRepository facilityReviewCommentRepository;
    private final FacilityReviewRepository facilityReviewRepository;
    private final UserRepository userRepository;

    // 시설 리뷰 댓글 조회
    @Transactional(readOnly = true)
    public FacilityReviewCommentListResponse getFacilityReviewComments(Long facilityReviewId) {
        List<FacilityReviewComment> commentList =
                facilityReviewCommentRepository.findAllByFacilityReview_FacilityReviewId(facilityReviewId);

        List<FacilityReviewCommentResponse> comments = commentList.stream()
                .map(FacilityReviewCommentResponse::from)
                .toList();

        return FacilityReviewCommentListResponse.from(comments);
    }

    // 시설 리뷰 댓글 작성
    @Transactional
    public FacilityReviewCommentResponse createFacilityReviewComment(User user, Long reviewId, FacilityReviewCommentRequest request) {
        FacilityReview review = facilityReviewRepository.findById(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND));

        FacilityReviewComment comment = request.toEntity(review, user);
        facilityReviewCommentRepository.save(comment);
        return FacilityReviewCommentResponse.from(comment);
    }

    // 시설 리뷰 댓글 수정
    @Transactional
    public FacilityReviewCommentResponse updateFacilityReviewComment(Long reviewId, Long commentId, FacilityReviewCommentUpdateRequest request) {
        FacilityReviewComment comment = facilityReviewCommentRepository.findByFacilityReviewCommentId(commentId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_COMMENT_NOT_FOUND));

        comment.updateCommentText(request.getCommentText());
        facilityReviewCommentRepository.save(comment);
        return FacilityReviewCommentResponse.from(comment);
    }

    // 시설 리뷰 댓글 삭제
    @Transactional
    public void deleteFacilityReviewComment(Long commentId) {
        facilityReviewCommentRepository.deleteById(commentId);
    }

}
