package com.efub.gogildong.facility.controller;

import com.efub.gogildong.facility.dto.request.FacilityReviewCommentRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewCommentUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewCommentResponse;
import com.efub.gogildong.facility.service.FacilityReviewCommentService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class FacilityReviewCommentController {

    private final UserRepository userRepository;
    private final FacilityReviewCommentService facilityReviewCommentService;

    // 시설 리뷰 댓글 조회
    @GetMapping
    public ResponseEntity<FacilityReviewCommentListResponse> getFacilityReviewComments(Authentication authentication,
                                                                                       @PathVariable("reviewId") Long reviewId) {
        FacilityReviewCommentListResponse response = facilityReviewCommentService.getFacilityReviewComments(authentication.getName(), reviewId);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 댓글 작성
    @PostMapping
    public ResponseEntity<FacilityReviewCommentResponse> createFacilityReviewComment(Authentication authentication,
                                                                                     @PathVariable("reviewId") Long reviewId,
                                                                                     @RequestBody @Valid FacilityReviewCommentRequest request) {
        FacilityReviewCommentResponse response = facilityReviewCommentService.createFacilityReviewComment(authentication.getName(), reviewId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 시설 리뷰 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<FacilityReviewCommentResponse> updateFacilityReviewComment(Authentication authentication,
                                                                                     @PathVariable("reviewId") Long reviewId,
                                                                                     @PathVariable("commentId") Long commentId,
                                                                                     @RequestBody @Valid FacilityReviewCommentUpdateRequest request) {
        FacilityReviewCommentResponse response = facilityReviewCommentService.updateFacilityReviewComment(authentication.getName(), reviewId, commentId, request);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteFacilityReviewComment(Authentication authentication,
                                            @PathVariable("reviewId") Long reviewId,
                                            @PathVariable("commentId") Long commentId) {
        facilityReviewCommentService.deleteFacilityReviewComment(authentication.getName(), reviewId, commentId);
        return ResponseEntity.noContent().build();
    }
}
