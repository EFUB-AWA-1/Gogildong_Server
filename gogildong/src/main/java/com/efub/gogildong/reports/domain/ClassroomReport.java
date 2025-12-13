package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.Classroom;
import com.efub.gogildong.facility.domain.DoorType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class ClassroomReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classroomReportId;

    @Column(nullable = false)
    private String classroomReportImage;

    @Column(nullable = false)
    private Float doorWidth;

    @Column(nullable = false)
    private Float doorHandleHeight;

    @Column(nullable = false)
    private Float minAisleWidth;

    @Column(nullable = false)
    private Boolean hasThreshold;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DoorType doorType;

    @OneToOne
    @Setter
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    @Setter
    private Classroom classroom;

    @Builder
    public ClassroomReport(String classroomReportImage, Float doorWidth, Float doorHandleHeight, Float minAisleWidth, Boolean hasThreshold,
                           DoorType doorType, Report report, Classroom classroom) {
        this.classroomReportImage = classroomReportImage;
        this.doorWidth = doorWidth;
        this.doorHandleHeight = doorHandleHeight;
        this.minAisleWidth = minAisleWidth;
        this.hasThreshold = hasThreshold;
        this.doorType = doorType;
        this.report = report;
        this.classroom = classroom;
    }
}
