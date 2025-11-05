package com.efub.gogildong.user.domain;

import com.efub.gogildong.schools.domain.School;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^[0-9\\-]{9,15}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "school_id")
    private School school;

    @Column(nullable = false)
    private int point = 0;

    @Column(nullable = false)
    private int total_score = 0;

    @Builder
    public User(String loginId, String password, String username, String email, String phone, UserRole role) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public void changeSchool(School school) {
        this.school = school;
    }

}
