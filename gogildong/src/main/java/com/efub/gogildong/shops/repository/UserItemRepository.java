package com.efub.gogildong.shops.repository;

import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.domain.UserItem;
import com.efub.gogildong.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    boolean existsByUserAndShopItem(User user, ShopItem shopItem);
    List<UserItem> findByUser(User user);
    List<UserItem> findByUserAndEquipped(User user, boolean equipped);
    Optional<UserItem> findByUserAndShopItem(User user, ShopItem shopItem);
}
