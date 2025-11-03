package com.efub.gogildong.facility.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long floorId;

    @Column(nullable = false)
    private String floorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Facility> facilities = new ArrayList<>();

    // 편의 메서드
    public void setBuilding(Building building) {
        this.building = building;
    }

    // 시설 추가
    public void addFacility(Facility facility) {
        facilities.add(facility);
        facility.setFloor(this);
    }
}
