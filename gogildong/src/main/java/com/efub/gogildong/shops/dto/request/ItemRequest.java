package com.efub.gogildong.shops.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {
    @NotNull(message = "아이템 아이디를 입력해주세요.")
    private Long itemId;
}
