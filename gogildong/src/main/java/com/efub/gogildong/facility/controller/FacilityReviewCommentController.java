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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class FacilityReviewCommentController {

    private final UserRepository userRepository;
    private final FacilityReviewCommentService facilityReviewCommentService;

    // 시설 리뷰 댓글 조회
    @GetMapping
    public ResponseEntity<FacilityReviewCommentListResponse> getFacilityReviewComments(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable("reviewId") Long reviewId) {
        FacilityReviewCommentListResponse response = facilityReviewCommentService.getFacilityReviewComments(reviewId);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 댓글 작성
    @PostMapping
    public ResponseEntity<FacilityReviewCommentResponse> createFacilityReviewComment(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                     @PathVariable("reviewId") Long reviewId,
                                                                                     @RequestBody @Valid FacilityReviewCommentRequest request) {
        //User user = userDetails.getUser();
        User mockUser = userRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        FacilityReviewCommentResponse response = facilityReviewCommentService.createFacilityReviewComment(mockUser, reviewId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 시설 리뷰 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<FacilityReviewCommentResponse> updateFacilityReviewComment(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                     @PathVariable("reviewId") Long reviewId,
                                                                                     @PathVariable("commentId") Long commentId,
                                                                                     @RequestBody @Valid FacilityReviewCommentUpdateRequest request) {
        FacilityReviewCommentResponse response = facilityReviewCommentService.updateFacilityReviewComment(reviewId, commentId, request);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteFacilityReviewComment(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("reviewId") Long reviewId,
                                            @PathVariable("commentId") Long commentId) {
        facilityReviewCommentService.deleteFacilityReviewComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
