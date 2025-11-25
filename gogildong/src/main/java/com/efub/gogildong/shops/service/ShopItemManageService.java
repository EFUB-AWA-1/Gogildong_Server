package com.efub.gogildong.shops.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.repository.ShopItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopItemManageService {
    private final ShopItemRepository shopItemRepository;

    @Transactional(readOnly = true)
    public ShopItem getShopItemByItemId(Long itemId){
        return shopItemRepository.findByItemId(itemId)
                .orElseThrow(()-> new GoGildongException(ExceptionCode.SHOP_ITEM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ShopItem> findDefaultItems(){
        return shopItemRepository.findByDefaultFlag(true);
    }

    @Transactional(readOnly = true)
    public List<ShopItem> findShopItemsByClotheType(ClotheType type){
        return shopItemRepository.findByType(type);
    }
}
