package com.efub.gogildong.facility.domain;

import com.efub.gogildong.reports.domain.ClassroomReport;
import com.efub.gogildong.reports.dto.response.classroom.ClassroomAggregateStat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classroomId;

    @Column(nullable = false)
    private Float doorWidth;

    @Column(nullable = false)
    private Float minDoorWidth;

    @Column(nullable = false)
    private Float maxDoorWidth;

    @Column(nullable = false)
    private Float doorHeight;

    @Column(nullable = false)
    private Float minAisleWidth;

    // 문턱 여부
    @Column(nullable = false)
    private Boolean hasThreshold;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DoorType doorType;

    // Facility와 1:1 매핑, 주인, 지연로딩
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomReport> classroomReports = new ArrayList<>();

    @Builder
    public Classroom(Float doorWidth, Float minDoorWidth, Float maxDoorWidth,
                    Float doorHeight, Float minAisleWidth, Boolean hasThreshold, DoorType doorType,  Facility facility) {
        this.doorWidth = doorWidth;
        this.minDoorWidth = minDoorWidth;
        this.maxDoorWidth = maxDoorWidth;
        this.doorHeight = doorHeight;
        this.minAisleWidth = minAisleWidth;
        this.hasThreshold = hasThreshold;
        this.doorType = doorType;
        this.facility = facility;
    }

    // 교실 제보 추가
    public void addClassroomReport(ClassroomReport classroomReport) {
        this.classroomReports.add(classroomReport);
        classroomReport.setClassroom(this);
    }

    // 제보 추가 시 교실 값 업데이트
    public void updateAggregate(ClassroomAggregateStat stat) {
        this.doorWidth = stat.getAvgDoorWidth();
        this.doorHeight = stat.getAvgDoorHeight();
        this.minAisleWidth = stat.getAvgMinAisleWidth();
        this.hasThreshold = stat.getAvgHasThreshold() >= 0.5;
        this.minDoorWidth = stat.getMinDoorWidth();
        this.maxDoorWidth = stat.getMaxDoorWidth();
    }
}
