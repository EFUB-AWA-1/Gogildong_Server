package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.Elevator;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class ElevatorReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long elevatorReportId;

    @Column(nullable = false)
    private String elevatorReportImage;

    @Column(nullable = false)
    private Float doorWidth;

    @Column(nullable = false)
    private Float interiorDepth;

    @Column(nullable = false)
    private Float maxControlPanelHeight;

    @OneToOne
    @Setter
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "elevator_id", nullable = false)
    @Setter
    private Elevator elevator;

    @Builder
    public ElevatorReport(String elevatorReportImage, Float doorWidth, Float  interiorDepth, Float maxControlPanelHeight, Report report, Elevator elevator) {
        this.elevatorReportImage = elevatorReportImage;
        this.doorWidth = doorWidth;
        this.interiorDepth =  interiorDepth;
        this.maxControlPanelHeight = maxControlPanelHeight;
        this.report = report;
        this.elevator = elevator;
    }
}
