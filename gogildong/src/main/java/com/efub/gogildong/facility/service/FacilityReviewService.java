package com.efub.gogildong.facility.service;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityReview;
import com.efub.gogildong.facility.dto.request.FacilityReviewRequest;
import com.efub.gogildong.facility.dto.request.FacilityReviewUpdateRequest;
import com.efub.gogildong.facility.dto.response.FacilityReviewListResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewResponse;
import com.efub.gogildong.facility.dto.response.FacilityReviewSummaryResponse;
import com.efub.gogildong.facility.respository.FacilityRepository;
import com.efub.gogildong.facility.respository.FacilityReviewRepository;
import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
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

    private final FacilityRepository facilityRepository;
    private final FacilityReviewRepository facilityReviewRepository;

    // 시설 리뷰 조회
    @Transactional(readOnly = true)
    public FacilityReviewListResponse getFacilityReviews(Long facilityId, int page) {
        Facility facility = facilityRepository.findByFacilityId(facilityId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<FacilityReview> reviewPage = facilityReviewRepository.findByFacility(facility, pageRequest);

        List<FacilityReviewSummaryResponse> reviewList = reviewPage.getContent().stream()
                .map(FacilityReviewSummaryResponse::from)
                .toList();

        return FacilityReviewListResponse.from(reviewPage, reviewList);
    }

    @Transactional
    // 시설 리뷰 작성
    public FacilityReviewResponse createFacilityReview(User user, FacilityReviewRequest request) {
        Facility facility = facilityRepository.findByFacilityId(request.getFacilityId())
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_NOT_FOUND));

        FacilityReview review = request.toEntity(facility, user);
        facilityReviewRepository.save(review);
        return FacilityReviewResponse.from(review);
    }

    @Transactional
    // 시설 리뷰 수정
    public FacilityReviewResponse updateFacilityReview(Long reviewId, FacilityReviewUpdateRequest request) {
        FacilityReview review = facilityReviewRepository.findByFacilityReviewId(reviewId)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.FACILITY_REVIEW_NOT_FOUND));

        review.updateReviewText(request.getReviewText());
        return FacilityReviewResponse.from(review);
    }

    @Transactional
    public void deleteFacilityReview(Long reviewId) {
        facilityReviewRepository.deleteById(reviewId);
    }
}
