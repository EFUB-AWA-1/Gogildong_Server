package com.efub.gogildong.schools.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long schoolId;

    @Column(nullable = false, unique = true)
    String schoolCode;

    @Column(nullable = false)
    String schoolName;

    @Column(nullable = false)
    String address;

    @Column(columnDefinition = "geography(Point,4326)", nullable = false)
    Point location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EduLevel eduLevel;

    @Builder
    public School(String schoolCode, String schoolName, String address, Point location, EduLevel eduLevel) {
        this.schoolCode = schoolCode;
        this.schoolName = schoolName;
        this.address = address;
        this.location = location;
        this.eduLevel = eduLevel;
    }
}
