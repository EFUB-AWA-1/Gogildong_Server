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
    private Float doorWidth;

    @Column(nullable = false)
    private Float minDoorWidth;

    @Column(nullable = false)
    private Float maxDoorWidth;

    @Column(nullable = false)
    private Float doorHeight;

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
                    Float doorWidth, Float minDoorWidth, Float maxDoorWidth,
                    Float doorHeight, Float toiletHeight, Boolean grabBar,  Facility facility) {
        this.isAccessible = isAccessible;
        this.gender = gender;
        this.doorType = doorType;
        this.doorWidth = doorWidth;
        this.minDoorWidth = minDoorWidth;
        this.maxDoorWidth = maxDoorWidth;
        this.doorHeight = doorHeight;
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
        this.doorWidth = stat.getAvgDoorWidth();
        this.doorHeight = stat.getAvgDoorHeight();
        this.toiletHeight = stat.getAvgToiletHeight();
        this.minDoorWidth = stat.getMinDoorWidth();
        this.maxDoorWidth = stat.getMaxDoorWidth();
        this.grabBar = stat.getAvgGrabBar() >= 0.5;
        this.isAccessible = stat.getAvgIsAccessible() >= 0.5;
    }
}
