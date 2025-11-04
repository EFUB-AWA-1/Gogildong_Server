package com.efub.gogildong.schools.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    @OneToMany(mappedBy = "school")
    List<SchoolTag> SchoolTags = new ArrayList<>();

    @Column(nullable = false, unique = true, length = 10)
    String adminCode;

    private static final ThreadLocalRandom RND = ThreadLocalRandom.current();

    private static String randomDigits(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(RND.nextInt(10));
        return sb.toString();
    }

    // 코드 매칭
    public boolean matchesAdminCode(String raw) {
        return raw != null && adminCode != null && adminCode.equals(raw);
    }

    @PrePersist
    void prePersist() {
        if (adminCode == null || adminCode.isBlank()) {
            adminCode = randomDigits(10);
        }
    }

    @Builder
    public School(String schoolCode, String schoolName, String address, Point location, EduLevel eduLevel, String adminCode) {
        this.schoolCode = schoolCode;
        this.schoolName = schoolName;
        this.address = address;
        this.location = location;
        this.eduLevel = eduLevel;
        this.adminCode = adminCode;
    }
}
