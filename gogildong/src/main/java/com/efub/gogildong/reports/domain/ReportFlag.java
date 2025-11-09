package com.efub.gogildong.reports.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReportFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportFlagId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ReportFlag(Report report, User user, String reason) {
        this.report = report;
        this.user = user;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }
}
