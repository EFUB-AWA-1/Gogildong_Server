package com.efub.gogildong.shops.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.domain.UserItem;
import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.dto.request.ItemRequest;
import com.efub.gogildong.shops.dto.response.EquippedItemListResponse;
import com.efub.gogildong.shops.dto.response.EquippedItemResponse;
import com.efub.gogildong.shops.dto.response.ItemResponse;
import com.efub.gogildong.shops.repository.ShopItemRepository;
import com.efub.gogildong.shops.repository.UserItemRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserItemService {
    private final EntityFinder finder;
    private final ShopItemManageService shopItemManageService;
    private final UserItemManageService userItemManageService;

    /*
    * 사용자가 착용한 아이템 조회
    * */
    @Transactional(readOnly = true)
    public EquippedItemListResponse getMyEquippedItems(String loginId) {
        User user = finder.getUserByLoginId(loginId);

        // 사용자가 착용한 아이템 조회
        List<UserItem> userItems = userItemManageService.findEquippedItemsByUser(user);

        // dto 변환
        List<EquippedItemResponse> equippedItemResponses = userItems.stream().map((item) -> EquippedItemResponse.from(item.getShopItem())).toList();
        return new EquippedItemListResponse(equippedItemResponses);
    }

    /*
    * 특정 아이템을 사용자 캐릭터에 입히기
    * */
    @Transactional
    public ItemResponse wearMyItem(String loginId, ItemRequest request) {
        User user = finder.getUserByLoginId(loginId);

        // item id로 아이템 조회
        ShopItem shopItem = shopItemManageService.getShopItemByItemId(request.getItemId());

        // 사용자가 해당 아이템을 가지고 있는지 확인 후, 사용자 아이템 반환
        UserItem userItem = userItemManageService.getOwnedUserItemOrThrow(user, shopItem);
        ClotheType type = shopItem.getType();

        // 사용자가 현재 착용하고 있는 아이템 조회
        List<UserItem> wearedItems = userItemManageService.findEquippedItemsByUser(user);

        // 착용하고 싶은 타입의 아이템 별로 벗어야 하는 타입 정의
        Map<ClotheType, List<ClotheType>> conflictMap = Map.of(
                ClotheType.HAT, List.of(ClotheType.HAT), // 모자 착용 원하면, 모자 벗어야 됨
                ClotheType.SHOES, List.of(ClotheType.SHOES), // 신발 착용 원하면, 신발 벗어야 됨
                ClotheType.DRESS, List.of(ClotheType.DRESS, ClotheType.TOP, ClotheType.BOTTOMS), // 드레스 착용 원하면, 드레스, 상의, 하의 벗어야 됨
                ClotheType.TOP, List.of(ClotheType.DRESS, ClotheType.TOP), // 상의 착용 원하면, 드레스, 상의 벗어야 됨
                ClotheType.BOTTOMS, List.of(ClotheType.DRESS, ClotheType.BOTTOMS) // 하의 착용 원하면, 드레스, 하의 벗어야 됨
        );

        // 현재 타입 기준 벗어야 하는 타입 리스트 반환
        List<ClotheType> conflictTypes = conflictMap.get(type);

        // 착용 원하는 아이템에 맞게 현재 입고 있는 아이템 벗기
        for (UserItem w : wearedItems) {
            if (conflictTypes.contains(w.getShopItem().getType())) {
                w.setEquipped(false);
            }
        }

        // 착용 원하는 아이템 입기
        userItem.setEquipped(true);
        return ItemResponse.fromShopItem(shopItem, true, true);
    }

    /*
    * 회원가입 시 기본 아이템 부여 후 착용합니다.
    * */
    @Transactional
    public void wearDefaultItemsForUser(User user){
        // 기본템 조회
        List<ShopItem> defaultItems = shopItemManageService.findDefaultItems();

        for(ShopItem shopItem : defaultItems){
            // 장착용 기본템 생성
            UserItem userItem = new UserItem(true);
            // 기본템 추가
            shopItem.addUserItem(userItem);
            user.addUserItem(userItem);
            userItemManageService.saveUserItem(userItem);
        }
    }


}
