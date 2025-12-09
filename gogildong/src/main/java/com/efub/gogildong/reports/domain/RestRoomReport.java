package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.DoorType;
import com.efub.gogildong.facility.domain.GenderType;
import com.efub.gogildong.facility.domain.Restroom;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class RestRoomReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restroomReportId;

    @Column(nullable = false)
    private String restroomReportImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false)
    private Boolean isAccessible;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DoorType doorType;

    @Column(nullable = false)
    private Float entranceDoorWidth;

    @Column(nullable = false)
    private Float entranceDoorHeight;

    @Column(nullable = false)
    private Float innerDoorWidth;

    @Column(nullable = false)
    private Float innerDoorHeight;

    @Column(nullable = false)
    private Float toiletHeight;

    @Column(nullable = false)
    private Boolean grabBar;

    @OneToOne
    @Setter
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "restroom_id", nullable = false)
    @Setter
    private Restroom restroom;

    @Builder
    public RestRoomReport(String restroomReportImage, DoorType doorType,
                          GenderType gender, Boolean isAccessible,
                          Float entranceDoorHeight, Float entranceDoorWidth,
                          Float innerDoorHeight, Float innerDoorWidth,
                          Float toiletHeight, Boolean grabBar, Report report, Restroom restroom) {
        this.restroomReportImage = restroomReportImage;
        this.gender = gender;
        this.isAccessible = isAccessible;
        this.entranceDoorHeight = entranceDoorHeight;
        this.entranceDoorWidth = entranceDoorWidth;
        this.innerDoorHeight = innerDoorHeight;
        this.innerDoorWidth = innerDoorWidth;
        this.toiletHeight = toiletHeight;
        this.grabBar = grabBar;
        this.report = report;
        this.restroom = restroom;
        this.doorType = doorType;
    }
}
