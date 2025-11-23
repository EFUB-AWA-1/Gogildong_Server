package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.Facility;
import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.global.domain.BaseEntity;
import com.efub.gogildong.schools.domain.RequestStatus;
import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(nullable = false)
    private int flagCount = 0;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityType reportType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RestRoomReport restRoomReport;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ElevatorReport elevatorReport;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ClassroomReport classroomReport;

    @Builder
    public Report(Boolean isPublic, FacilityType reportType, ReportStatus status, User user, Facility facility) {
        this.isPublic = isPublic;
        this.reportType = reportType;
        this.status = status;
        this.user = user;
        this.facility = facility;
    }

    // 신고 횟수 추가
    public void addFlag() {
        flagCount++;
    }

    // 공개 여부 변경
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
