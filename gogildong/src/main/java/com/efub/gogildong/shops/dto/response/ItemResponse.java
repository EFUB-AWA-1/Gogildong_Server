package com.efub.gogildong.shops.dto.response;

import com.efub.gogildong.shops.domain.ShopItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {
    private final Long itemId;
    private final String name;
    private final String type;
    private final int price;
    private final String itemImage;
    private final boolean hasItem;
    private final Boolean equip;

    public static ItemResponse fromShopItem(ShopItem shopItem) {
        return ItemResponse.builder()
                .itemId(shopItem.getItemId())
                .name(shopItem.getName())
                .type(shopItem.getType().name().toLowerCase())
                .price(shopItem.getPrice())
                .itemImage(shopItem.getItemImage())
                .hasItem(true)
                .build();
    }

    public static ItemResponse fromShopItem(ShopItem item, boolean hasItem) {
        return ItemResponse.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .type(item.getType().name().toLowerCase())
                .price(item.getPrice())
                .itemImage(item.getItemImage())
                .hasItem(hasItem)
                .build();
    }

    public static ItemResponse fromShopItem(ShopItem item, boolean hasItem, boolean equip) {
        return ItemResponse.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .type(item.getType().name().toLowerCase())
                .price(item.getPrice())
                .itemImage(item.getItemImage())
                .hasItem(hasItem)
                .equip(equip)
                .build();
    }
}
