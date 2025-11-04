package com.efub.gogildong.facility.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String gender;

    @Column(nullable = false)
    private String doorType;

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

    @Builder
    public Restroom(Boolean isAccessible, String gender, String doorType,
                    Float doorWidth, Float minDoorWidth, Float maxDoorWidth,
                    Float doorHeight, Float toiletHeight, Boolean grabBar) {
        this.isAccessible = isAccessible;
        this.gender = gender;
        this.doorType = doorType;
        this.doorWidth = doorWidth;
        this.minDoorWidth = minDoorWidth;
        this.maxDoorWidth = maxDoorWidth;
        this.doorHeight = doorHeight;
        this.toiletHeight = toiletHeight;
        this.grabBar = grabBar;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
