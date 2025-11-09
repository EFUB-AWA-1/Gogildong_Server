package com.efub.gogildong.facility.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class FacilityReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityReviewId;

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Integer commentCount;

    // facility와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    // 유저와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 리뷰댓글과 1:n 매핑, 지연로딩 + 고아객체제거
    @OneToMany(mappedBy = "facilityReview", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FacilityReviewComment> comments = new ArrayList<>();

    // 리뷰좋아요와 1:n 매핑, 지연로딩 + 고아객체제거
    @OneToMany(mappedBy = "facilityReview", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FacilityReviewLike> likes = new ArrayList<>();

    @Builder
    public FacilityReview(String reviewText, Integer likeCount, Integer commentCount, Facility facility, User user) {
        this.reviewText = reviewText;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.facility = facility;
        this.user = user;
    }

    // 리뷰 내용 수정
    public void updateReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
