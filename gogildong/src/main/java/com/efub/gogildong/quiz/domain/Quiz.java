package com.efub.gogildong.quiz.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String question;

    @OneToOne
    @JoinColumn(name = "correct_choice_id")
    private QuizChoice correctChoice;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizChoice> choices;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
