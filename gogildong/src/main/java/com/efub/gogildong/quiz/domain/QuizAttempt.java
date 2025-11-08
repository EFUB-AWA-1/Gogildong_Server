package com.efub.gogildong.quiz.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "quiz_attempt",
        uniqueConstraints = @UniqueConstraint(name = "uq_attempt_user_quiz", columnNames = {"user_id","quiz_id"}))
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_choice_id", nullable = false)
    private QuizChoice selectedChoice;

    @Column(nullable = false)
    private Boolean isCorrect;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAnswer(QuizChoice newChoice, boolean correct) {
        this.selectedChoice = newChoice;
        this.isCorrect = correct;
    }
}
