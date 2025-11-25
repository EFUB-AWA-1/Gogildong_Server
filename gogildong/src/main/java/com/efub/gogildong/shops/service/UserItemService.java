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

    @Transactional(readOnly = true)
    public EquippedItemListResponse getMyEquippedItems(String loginId) {
        User user = finder.getUserByLoginId(loginId);
        List<UserItem> userItems = userItemManageService.findEquippedItemsByUser(user);
        List<EquippedItemResponse> equippedItemResponses = userItems.stream().map((item) -> EquippedItemResponse.from(item.getShopItem())).toList();
        return new EquippedItemListResponse(equippedItemResponses);
    }

    @Transactional
    public ItemResponse wearMyItem(String loginId, ItemRequest request) {
        User user = finder.getUserByLoginId(loginId);
        ShopItem shopItem = shopItemManageService.getShopItemByItemId(request.getItemId());
        UserItem userItem = userItemManageService.getOwnedUserItemOrThrow(user, shopItem);
        ClotheType type = shopItem.getType();
        List<UserItem> wearedItems = userItemManageService.findEquippedItemsByUser(user);

        Map<ClotheType, List<ClotheType>> conflictMap = Map.of(
                ClotheType.HAT, List.of(ClotheType.HAT),
                ClotheType.SHOES, List.of(ClotheType.SHOES),
                ClotheType.DRESS, List.of(ClotheType.DRESS, ClotheType.TOP, ClotheType.BOTTOMS),
                ClotheType.TOP, List.of(ClotheType.DRESS, ClotheType.TOP),
                ClotheType.BOTTOMS, List.of(ClotheType.DRESS, ClotheType.BOTTOMS)
        );

        List<ClotheType> conflictTypes = conflictMap.get(type);
        for (UserItem w : wearedItems) {
            if (conflictTypes.contains(w.getShopItem().getType())) {
                w.setEquipped(false);
            }
        }

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
