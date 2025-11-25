package com.efub.gogildong.shops.repository;

import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.domain.ShopItem;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    boolean existsByName(String name);
    List<ShopItem> findByDefaultFlag(boolean defaultFlag);
    List<ShopItem> findByType(ClotheType type);
    Optional<ShopItem> findByItemId(Long itemId);
}
