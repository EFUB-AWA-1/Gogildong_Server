package com.efub.gogildong.shops.controller;

import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.dto.request.ItemRequest;
import com.efub.gogildong.shops.dto.response.ItemListResponse;
import com.efub.gogildong.shops.dto.response.PurchaseResponse;
import com.efub.gogildong.shops.service.ShopItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ShopItemController {

    private final ShopItemService shopItemService;

    /*
    * 상점에 있는 전체 아이템 조회
    * */
    @GetMapping
    public ResponseEntity<ItemListResponse> getItemList(@RequestParam("type")ClotheType type,
                                                        Authentication authentication) {
        return ResponseEntity.ok(shopItemService.getItemList(type, authentication.getName()));
    }

    /*
    * 상점 내 아이템 구매
    * */
    @PostMapping("/me")
    public ResponseEntity<PurchaseResponse> purchaseItem(@Valid @RequestBody ItemRequest request,
                                                         Authentication authentication) {
        return ResponseEntity.ok(shopItemService.purchaseItem(request, authentication.getName()));
    }

    /*
    * 사용자가 보유 중인 아이템 목록 조회
    * */
    @GetMapping("/me")
    public ResponseEntity<ItemListResponse> getMyItemList(Authentication authentication) {
        return ResponseEntity.ok(shopItemService.getMyItemList(authentication.getName()));
    }
}
