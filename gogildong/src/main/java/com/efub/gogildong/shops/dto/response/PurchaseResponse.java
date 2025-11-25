package com.efub.gogildong.shops.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PurchaseResponse {
    private final String message = "아이템 구매 완료";
    private final ItemResponse item;
    private int point;

    @Builder
    public PurchaseResponse(ItemResponse item, int point) {
        this.item = item;
        this.point = point;
    }
}
