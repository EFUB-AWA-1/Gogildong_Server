package com.efub.gogildong.facility.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Facility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @Column(nullable = false)
    private String facilityName;

    @Column(nullable = false)
    private String facilityNickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;

    @Column(nullable = true)
    private String reviewSummary;

    // 건물층과 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    // Restroom과 1:1 매핑, 지연로딩 + 고아객체제거
    @OneToOne(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Restroom restroom;

    // FacilityReview과 1:n 매핑, 지연로딩 + 고아객체제거
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FacilityReview> reviews = new ArrayList<>();

    @Builder
    public Facility(String facilityName, String facilityNickname, FacilityType facilityType,
        String reviewSummary, Floor floor) {
        this.facilityName = facilityName;
        this.facilityNickname = facilityNickname;
        this.facilityType = facilityType;
        this.reviewSummary = reviewSummary;
        this.floor = floor;
    }

    // 리뷰 추가
    public void addReview(FacilityReview review) {
        reviews.add(review);
    }

    // 리뷰 삭제
    public void removeReview(FacilityReview review) {
        reviews.remove(review);
    }
}
