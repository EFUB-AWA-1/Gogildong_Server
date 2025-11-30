package com.efub.gogildong.facility.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FacilityReviewComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityReviewCommentId;

    @Column(nullable = false)
    private String commentText;

    @Column(nullable = false)
    private int flagCount = 0;

    // 리뷰와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_review_id", nullable = false)
    private FacilityReview facilityReview;

    // 유저와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public FacilityReviewComment(String commentText, FacilityReview facilityReview, User user) {
        this.commentText = commentText;
        this.facilityReview = facilityReview;
        this.user = user;
    }

    // 리뷰 댓글 내용 수정
    public void updateCommentText(String commentText) {
        this.commentText = commentText;
    }

    // 신고 횟수 추가
    public void addFlag() {
        flagCount++;
    }
}
