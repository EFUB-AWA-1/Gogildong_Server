package com.efub.gogildong.facility.domain;

import com.efub.gogildong.schools.domain.School;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buildingId;

    @Column(nullable = false, unique = true)
    private String buildingName;

    // 학교와 n:1 매핑, 주인, 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    // 건물층과 1:n 매핑, 지연로딩 + 고아객체제거
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();

    // 편의 메서드
    // 건물층 추가
    public void addFloor(Floor floor) {
        floors.add(floor);
        floor.setBuilding(this);
    }
}
