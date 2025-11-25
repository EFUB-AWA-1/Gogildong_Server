package com.efub.gogildong.shops.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemListResponse {
    private final List<ItemResponse> items;
}
