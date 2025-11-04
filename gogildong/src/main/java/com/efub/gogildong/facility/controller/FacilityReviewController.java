package com.efub.gogildong.facility.controller;

import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.dto.request.FacilityReviewRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewResponse;
import com.efub.gogildong.facility.service.FacilityReviewService;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.domain.UserRole;
import com.efub.gogildong.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facilities/reviews")
@RequiredArgsConstructor
public class FacilityReviewController {


    private final FacilityReviewService facilityReviewService;
    private final UserRepository userRepository;

    // 시설 리뷰 조회
    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityReviewListResponse> getFacilityReviews(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable("facilityId") Long facilityId,
                                                                         @RequestParam(defaultValue = "0") int page) {
        FacilityReviewListResponse response = facilityReviewService.getFacilityReviews(facilityId, page);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 작성
    @PostMapping
    public ResponseEntity<FacilityReviewResponse> createFacilityReview(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @RequestBody @Valid FacilityReviewRequest request) {
        //User user = userDetails.getUser();
        User mockUser = userRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        FacilityReviewResponse response = facilityReviewService.createFacilityReview(mockUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 시설 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<FacilityReviewResponse> updateFacilityReview(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @PathVariable("reviewId") Long reviewId,
                                                                       @RequestBody @Valid FacilityReviewUpdateRequest request) {
        FacilityReviewResponse response = facilityReviewService.updateFacilityReview(reviewId, request);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<FacilityReviewResponse> deleteFacilityReview(//@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable("reviewId") Long reviewId) {
        facilityReviewService.deleteFacilityReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
