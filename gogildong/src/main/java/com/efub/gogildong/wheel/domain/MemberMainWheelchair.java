package com.efub.gogildong.wheel.domain;

import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "member_main_wheelchair",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_member_wheelchair",
                        columnNames = {"user_id", "wheelchair_id"}
                )
        }
)
public class MemberMainWheelchair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_main_wheelchair_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wheelchair_id")
    private Wheelchair wheelchair;

    public MemberMainWheelchair(User user, Wheelchair wheelchair) {
        this.user = user;
        this.wheelchair = wheelchair;
    }
}
