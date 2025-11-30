package com.efub.gogildong.facility.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class FacilityReviewCommentFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentFlagId;

    @ManyToOne(fetch = FetchType.LAZY)
    private FacilityReviewComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public FacilityReviewCommentFlag(FacilityReviewComment comment, User user, String reason) {
        this.comment = comment;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
