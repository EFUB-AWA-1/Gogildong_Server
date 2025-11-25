package com.efub.gogildong.shops.controller;

import com.efub.gogildong.shops.dto.request.ItemRequest;
import com.efub.gogildong.shops.dto.response.EquippedItemListResponse;
import com.efub.gogildong.shops.dto.response.ItemResponse;
import com.efub.gogildong.shops.service.UserItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class UserItemController {

    private final UserItemService userItemService;

    /*
    * 사용자가 착용한 아이템 조회
    * */
    @GetMapping("/me")
    public ResponseEntity<EquippedItemListResponse> getMyEquippedItems(Authentication authentication) {
        return ResponseEntity.ok(userItemService.getMyEquippedItems(authentication.getName()));
    }

    /*
    * 아이템 착용
    * */
    @PostMapping("/me")
    public ResponseEntity<ItemResponse> wearMyItem(Authentication authentication, @Valid @RequestBody ItemRequest request){
        return ResponseEntity.ok(userItemService.wearMyItem(authentication.getName(), request));
    }
}
