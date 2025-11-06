package com.efub.gogildong.reports.domain;

import com.efub.gogildong.facility.domain.FacilityType;
import com.efub.gogildong.global.domain.BaseEntity;
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

    @Column(nullable = false)
    private int flagCount = 0;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityType reportType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @Builder
    public Report(Boolean isPublic, FacilityType reportType, User user) {
        this.isPublic = isPublic;
        this.reportType = reportType;
        this.user = user;
    }
}
