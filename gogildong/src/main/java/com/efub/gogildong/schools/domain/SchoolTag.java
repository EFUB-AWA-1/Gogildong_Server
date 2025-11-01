package com.efub.gogildong.schools.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SchoolTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TagName tagName;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

}
