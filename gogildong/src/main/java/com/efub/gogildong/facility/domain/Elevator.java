package com.efub.gogildong.facility.domain;

import com.efub.gogildong.reports.domain.ElevatorReport;
import com.efub.gogildong.reports.dto.response.elevator.ElevatorAggregateStat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Elevator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long elevatorId;

    @Column(nullable = false)
    private Float doorWidth;

    @Column(nullable = false)
    private Float minDoorWidth;

    @Column(nullable = false)
    private Float maxDoorWidth;

    @Column(nullable = false)
    private Float doorHeight;

    @Column(nullable = false)
    private Float maxControlPanelHeight;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "elevator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElevatorReport> elevatorReports = new ArrayList<>();

    @Builder
    public Elevator(Float doorWidth, Float minDoorWidth, Float maxDoorWidth,
                    Float doorHeight, Float maxControlPanelHeight, Facility facility) {
        this.doorWidth = doorWidth;
        this.minDoorWidth = minDoorWidth;
        this.maxDoorWidth = maxDoorWidth;
        this.doorHeight = doorHeight;
        this.maxControlPanelHeight = maxControlPanelHeight;
        this.facility = facility;
    }

    // 엘리베이터 제보 추가
    public void addElevatorReport(ElevatorReport elevatorReport) {
        this.elevatorReports.add(elevatorReport);
        elevatorReport.setElevator(this);
    }

    // 제보 추가 시 엘리베이터 값 업데이트
    public void updateAggregate(ElevatorAggregateStat stat) {
        this.doorWidth = stat.getAvgDoorWidth();
        this.doorHeight = stat.getAvgDoorHeight();
        this.maxControlPanelHeight = stat.getAvgMaxControlPanelHeight();
        this.minDoorWidth = stat.getMinDoorWidth();
        this.maxDoorWidth = stat.getMaxDoorWidth();
    }
}
