package com.efub.gogildong.schools.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SchoolViewRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReasonCategory reasonCategory;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Builder
    public SchoolViewRequest(RequestStatus status, ReasonCategory reasonCategory, String reason, String phoneNumber, LocalDateTime requestedAt, User user, School school) {
        this.status = status;
        this.reasonCategory = reasonCategory;
        this.reason = reason;
        this.phoneNumber = phoneNumber;
        this.requestedAt = requestedAt;
        this.user = user;
        this.school = school;
    }
}
