package com.efub.gogildong.wheel.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wheelchair")
public class Wheelchair extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wheelchair_id")
    private Long id;

    @Column(name = "wheelchair_name", nullable = false)
    private String wheelchairName;

    @Column(nullable = false)
    private int width;

    @Column(name = "max_threshold", nullable = false)
    private int maxThreshold;

    // 이 휠체어를 현재 즐겨찾기하고 있는 회원 수
    @Column(name = "bookmark_count", nullable = false)
    private int bookmarkCount = 0;

    public Wheelchair(String wheelchairName, int width, int maxThreshold) {
        this.wheelchairName = wheelchairName;
        this.width = width;
        this.maxThreshold = maxThreshold;
        this.bookmarkCount = 0;
    }

    public void increaseBookmarkCount() {
        this.bookmarkCount++;
    }

    public void decreaseBookmarkCount() {
        if (this.bookmarkCount > 0) {
            this.bookmarkCount--;
        }
    }
}
