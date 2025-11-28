package com.efub.gogildong.facility.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    private Restroom restroom;

    // Elevator과 1:1 매핑, 지연로딩 + 고아객체제거
    @OneToOne(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Setter
    private Elevator elevator;

    // Classroom과 1:1 매핑, 지연로딩 + 고아객체제거
    @OneToOne(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Setter
    private Classroom classroom;

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

    // 시설 별칭 업데이트
    public void updateNickname(String facilityName) {
        this.facilityNickname = facilityNickname;
    }

    // 시설 리뷰 바탕 요약 업데이트
    public void updateSummary(String reviewSummary) { this.reviewSummary = reviewSummary; }
}
