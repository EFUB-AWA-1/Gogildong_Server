package com.efub.gogildong.shops.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.domain.UserItem;
import com.efub.gogildong.shops.repository.UserItemRepository;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserItemManageService {

    private final UserItemRepository userItemRepository;

    @Transactional(readOnly = true)
    public boolean existsUserItemByUserAndShopItem(User user, ShopItem shopItem){
        return userItemRepository.existsByUserAndShopItem(user, shopItem);
    }

    @Transactional(readOnly = true)
    public List<UserItem> findUserItemsByUser(User user){
        return userItemRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<UserItem> findEquippedItemsByUser(User user){
        return userItemRepository.findByUserAndEquipped(user, true);
    }

    @Transactional
    public void saveUserItem(UserItem userItem){
        userItemRepository.save(userItem);
    }

    @Transactional
    public UserItem getOwnedUserItemOrThrow(User user, ShopItem shopItem){
        return userItemRepository.findByUserAndShopItem(user, shopItem)
                .orElseThrow(() -> new GoGildongException(ExceptionCode.SHOP_ITEM_NOT_OWNED));
    }
}
