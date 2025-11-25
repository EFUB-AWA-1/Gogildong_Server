package com.efub.gogildong.shops.domain;

import com.efub.gogildong.global.domain.BaseEntity;
import com.efub.gogildong.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class UserItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userItemId;

    @Getter
    @Setter
    private boolean equipped;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @Setter
    @Getter
    private ShopItem shopItem;

    @Builder
    public UserItem(boolean equipped){
        this.equipped = equipped;
    }

}
