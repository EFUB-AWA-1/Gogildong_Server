package com.efub.gogildong.shops.dto.response;

import com.efub.gogildong.shops.domain.ShopItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EquippedItemResponse {
    private final Long itemId;
    private final String type;
    private final String name;
    private final String imageUrl;

    public static EquippedItemResponse from(ShopItem item) {
        return EquippedItemResponse.builder()
                .itemId(item.getItemId())
                .type(item.getType().name().toLowerCase())
                .name(item.getName())
                .imageUrl(item.getItemImage())
                .build();
    }
}