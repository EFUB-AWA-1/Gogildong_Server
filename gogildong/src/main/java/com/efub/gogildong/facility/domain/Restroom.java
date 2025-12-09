package com.efub.gogildong.facility.domain;

import com.efub.gogildong.reports.domain.RestRoomReport;
import com.efub.gogildong.reports.dto.response.restroom.RestRoomAggregateStat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Restroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restroomId;

    // 장애인 화장실 여부
    @Column(nullable = false)
    private Boolean isAccessible;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DoorType doorType;

    @Column(nullable = false)
    private Float entranceDoorWidth;
    @Column(nullable = false)
    private Float minEntranceDoorWidth;
    @Column(nullable = false)
    private Float maxEntranceDoorWidth;
    @Column(nullable = false)
    private Float entranceDoorHeight;

    @Column(nullable = false)
    private Float innerDoorWidth;
    @Column(nullable = false)
    private Float minInnerDoorWidth;
    @Column(nullable = false)
    private Float maxInnerDoorWidth;
    @Column(nullable = false)
    private Float innerDoorHeight;

    @Column(nullable = false)
    private Float toiletHeight;

    @Column(nullable = false)
    private Boolean grabBar;

    // Facility와 1:1 매핑, 주인, 지연로딩
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestRoomReport> restRoomReports = new ArrayList<>();

    @Builder
    public Restroom(Boolean isAccessible, GenderType gender, DoorType doorType,
                    Float toiletHeight, Boolean grabBar,  Facility facility,
                    Float minEntranceDoorWidth, Float maxEntranceDoorWidth,
                    Float minInnerDoorWidth, Float maxInnerDoorWidth,
                    Float innerDoorHeight, Float innerDoorWidth, Float entranceDoorWidth, Float entranceDoorHeight) {
        this.isAccessible = isAccessible;
        this.gender = gender;
        this.doorType = doorType;
        this.entranceDoorHeight = entranceDoorHeight;
        this.innerDoorHeight = innerDoorHeight;
        this.innerDoorWidth = innerDoorWidth;
        this.entranceDoorWidth = entranceDoorWidth;
        this.maxEntranceDoorWidth = maxEntranceDoorWidth;
        this.minEntranceDoorWidth = minEntranceDoorWidth;
        this.maxInnerDoorWidth = maxInnerDoorWidth;
        this.minInnerDoorWidth = minInnerDoorWidth;
        this.toiletHeight = toiletHeight;
        this.grabBar = grabBar;
        this.facility = facility;
    }

    // 화장실 제보 추가
    public void addRestRoomReport(RestRoomReport restRoomReport) {
        this.restRoomReports.add(restRoomReport);
        restRoomReport.setRestroom(this);
    }

    // 제보 추가 시 화장실 값 업데이트
    public void updateAggregate(RestRoomAggregateStat stat) {
        this.gender = stat.getMajorityGender();
        this.toiletHeight = stat.getAvgToiletHeight();
        this.grabBar = stat.getAvgGrabBar() >= 0.5;
        this.isAccessible = stat.getAvgIsAccessible() >= 0.5;

        // 출입문 업데이트
        this.entranceDoorWidth = stat.getAvgEntranceDoorWidth();
        this.entranceDoorHeight = stat.getAvgEntranceDoorHeight();
        this.minEntranceDoorWidth = stat.getMinEntranceDoorWidth();
        this.maxEntranceDoorWidth = stat.getMaxEntranceDoorWidth();

        // 내부문 업데이트
        this.innerDoorWidth = stat.getAvgInnerDoorWidth();
        this.innerDoorHeight = stat.getAvgInnerDoorHeight();
        this.minInnerDoorWidth = stat.getMinInnerDoorWidth();
        this.maxInnerDoorWidth = stat.getMaxInnerDoorWidth();
    }
}
