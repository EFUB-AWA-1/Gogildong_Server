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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facilities/reviews")
@RequiredArgsConstructor
public class FacilityReviewController {


    private final FacilityReviewService facilityReviewService;
    private final UserRepository userRepository;

    // 시설 리뷰 조회
    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityReviewListResponse> getFacilityReviews(Authentication authentication,
                                                                         @PathVariable("facilityId") Long facilityId,
                                                                         @RequestParam(defaultValue = "0") int page) {
        FacilityReviewListResponse response = facilityReviewService.getFacilityReviews(authentication.getName(), facilityId, page);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 작성
    @PostMapping
    public ResponseEntity<FacilityReviewResponse> createFacilityReview(Authentication authentication,
                                                                       @RequestBody @Valid FacilityReviewRequest request) {
        FacilityReviewResponse response = facilityReviewService.createFacilityReview(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 시설 리뷰 수정
    @PatchMapping("/{reviewId}")
    public ResponseEntity<FacilityReviewResponse> updateFacilityReview(Authentication authentication,
                                                                       @PathVariable("reviewId") Long reviewId,
                                                                       @RequestBody @Valid FacilityReviewUpdateRequest request) {
        FacilityReviewResponse response = facilityReviewService.updateFacilityReview(authentication.getName(), reviewId, request);
        return ResponseEntity.ok(response);
    }

    // 시설 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteFacilityReview(Authentication authentication,
                                                                        @PathVariable("reviewId") Long reviewId) {
        facilityReviewService.deleteFacilityReview(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
