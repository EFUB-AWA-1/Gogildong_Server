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
    private Float doorHeight;

    @Column(nullable = false)
    private Float maxControlPanelHeight;

    private String note;

    @OneToOne
    @Setter
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "elevator_id", nullable = false)
    @Setter
    private Elevator elevator;

    @Builder
    public ElevatorReport(String elevatorReportImage, Float doorWidth, Float doorHeight, Float maxControlPanelHeight, String note, Report report, Elevator elevator) {
        this.elevatorReportImage = elevatorReportImage;
        this.doorWidth = doorWidth;
        this.doorHeight = doorHeight;
        this.maxControlPanelHeight = maxControlPanelHeight;
        this.note = note;
        this.report = report;
        this.elevator = elevator;
    }
}
