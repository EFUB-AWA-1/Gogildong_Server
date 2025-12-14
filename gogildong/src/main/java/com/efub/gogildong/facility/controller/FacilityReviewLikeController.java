package com.efub.gogildong.facility.controller;

import com.efub.gogildong.facility.dto.response.FacilityReviewLikeResponse;
import com.efub.gogildong.facility.service.FacilityReviewLikeService;
import com.efub.gogildong.user.domain.User;
import com.efub.gogildong.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews/{reviewId}/likes")
@RequiredArgsConstructor
public class FacilityReviewLikeController {

    private final UserRepository userRepository;
    private final FacilityReviewLikeService facilityReviewLikeService;

    @PostMapping
    public ResponseEntity<Void> createFacilityReviewLike(Authentication authentication,
                                                         @PathVariable("reviewId") Long reviewId) {
        facilityReviewLikeService.createFacilityReviewLike(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<Void> deleteFacilityReviewLike(Authentication authentication,
                                                         @PathVariable("reviewId") Long reviewId) {
        facilityReviewLikeService.deleteFacilityReviewLike(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
