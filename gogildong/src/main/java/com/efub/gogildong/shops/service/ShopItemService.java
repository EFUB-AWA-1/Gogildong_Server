package com.efub.gogildong.shops.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.domain.UserItem;
import com.efub.gogildong.shops.dto.request.ItemRequest;
import com.efub.gogildong.shops.dto.response.ItemListResponse;
import com.efub.gogildong.shops.dto.response.ItemResponse;
import com.efub.gogildong.shops.dto.response.PurchaseResponse;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopItemService {
    private final EntityFinder finder;
    private final ShopItemManageService shopItemManageService;
    private final UserItemManageService userItemManageService;
    private final CoinService coinService;

    /*
    * 상점 내 전체 아이템 조회
    * */
    @Transactional(readOnly = true)
    public ItemListResponse getItemList(ClotheType type, String loginId){
        User user = finder.getUserByLoginId(loginId);

        // 요청한 타입에 해당되는 모든 아이템 조회
        List<ShopItem> shopItems = shopItemManageService.findShopItemsByClotheType(type);

        // 아이템 소지 여부 + 아이템 정보 리스트로 정리
        List<ItemResponse> itemResponses = shopItems.stream().map((item)->{
            boolean hasItem = userItemManageService.existsUserItemByUserAndShopItem(user, item);
            return ItemResponse.fromShopItem(item, hasItem);
        }).toList();
        return new ItemListResponse(itemResponses);
    }

    /*
    * 아이템 구매
    * */
    @Transactional
    public PurchaseResponse purchaseItem(ItemRequest request, String loginId){
        User user = finder.getUserByLoginId(loginId);
        Long itemId = request.getItemId();

        // 해당되는 아이템이 없을 경우 에러 처리
        ShopItem shopItem = shopItemManageService.getShopItemByItemId(itemId);

        // 이미 구매한 아이템인 경우 에러 처리
        if(userItemManageService.existsUserItemByUserAndShopItem(user, shopItem)){
            throw new GoGildongException(ExceptionCode.SHOP_ITEM_ALREADY_HAVE);
        }

        // 아이템 가격만큼 지갑 속 금액 차감
        int price = shopItem.getPrice();
        coinService.spendCoin(user, price);

        UserItem userItem = new UserItem(false);
        shopItem.addUserItem(userItem);
        user.addUserItem(userItem);
        ItemResponse itemResponse = ItemResponse.fromShopItem(shopItem);
        return new PurchaseResponse(itemResponse, user.getCoin());
    }

    /*
    * 사용자가 보유중인 아이템 조회
    * */
    @Transactional(readOnly = true)
    public ItemListResponse getMyItemList(String loginId){
        User user = finder.getUserByLoginId(loginId);

        // 사용자가 보유중인 아이템 목록 조회
        List<UserItem> userItems = userItemManageService.findUserItemsByUser(user);

        // 착용 여부 포함한 아이템 정보 반환
        List<ItemResponse> itemResponses = userItems.stream().map((item)->{
            return ItemResponse.fromShopItem(item.getShopItem(), true, item.isEquipped());
        }).toList();
        return new ItemListResponse(itemResponses);
    }
}
