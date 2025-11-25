package com.efub.gogildong.shops.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class EquippedItemListResponse {
    private List<EquippedItemResponse> equippedItem;
}
