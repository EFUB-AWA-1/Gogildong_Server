package com.efub.gogildong.shops.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ShopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, unique = true)
    private String name;

    private String itemImage;

    private String wearingItemImage;

    @Enumerated(EnumType.STRING)
    private ClotheType type;

    private int price;

    private boolean defaultFlag;

    @OneToMany(mappedBy = "shopItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserItem> userItemList = new ArrayList<>();

    public void addUserItem(UserItem userItem) {
        userItemList.add(userItem);
        userItem.setShopItem(this);
    }

    @Builder
    public ShopItem(String name, String itemImage, ClotheType type, int price, boolean defaultFlag, String wearingItemImage) {
        this.name = name;
        this.itemImage = itemImage;
        this.type = type;
        this.price = price;
        this.defaultFlag = defaultFlag;
        this.wearingItemImage = wearingItemImage;
    }
}
