package com.efub.gogildong.facility.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column
    @Setter
    private String floorPlanImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Facility> facilities = new ArrayList<>();

    // 시설 추가
    public void addFacility(Facility facility) {
        facilities.add(facility);
    }
}
