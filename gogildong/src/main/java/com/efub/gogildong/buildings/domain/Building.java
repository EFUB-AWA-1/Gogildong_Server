package com.efub.gogildong.buildings.domain;

import com.efub.gogildong.schools.domain.School;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long buildingId;

    @Column(nullable = false)
    String buildingName;

    @ManyToOne
    @JoinColumn(name = "school_id")
    School school;
}
