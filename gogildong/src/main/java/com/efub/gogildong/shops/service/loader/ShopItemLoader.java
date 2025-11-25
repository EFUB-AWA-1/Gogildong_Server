package com.efub.gogildong.shops.service.loader;

import com.efub.gogildong.shops.domain.ClotheType;
import com.efub.gogildong.shops.domain.ShopItem;
import com.efub.gogildong.shops.repository.ShopItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopItemLoader implements ApplicationRunner {
    private final ShopItemRepository shopItemRepository;

    @Value("${item.image-base-url}")
    private String baseImageUrl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // --- 기본 아이템 ---
        insertIfNotExists("기본 티셔츠", "default_top.png", ClotheType.TOP, 0, true);
        insertIfNotExists("기본 모자", "default_hat.png", ClotheType.HAT, 0, true);
        insertIfNotExists("기본 원피스", "default_dress.png", ClotheType.DRESS, 0, true);
        insertIfNotExists("기본 신발", "default_shoes.png", ClotheType.SHOES, 0, true);

        // --- 일반 상점 아이템 ---
        insertIfNotExists("멋진 청바지", "jeans.png", ClotheType.BOTTOMS, 500, false);
        insertIfNotExists("레드 스니커즈", "red_sneakers.png", ClotheType.SHOES, 300, false);
        insertIfNotExists("블루 후드티", "blue_hoodie.png", ClotheType.TOP, 400, false);
        insertIfNotExists("민트 원피스", "mint_dress.png", ClotheType.DRESS, 600, false);
    }

    private void insertIfNotExists(String name, String fileName, ClotheType type, int price, boolean defaultFlag){
        if(shopItemRepository.existsByName(name)){
            return;
        }
        ShopItem shopItem = ShopItem.builder()
                .name(name)
                .type(type)
                .price(price)
                .defaultFlag(defaultFlag)
                .itemImage(baseImageUrl + fileName)
                .build();
        shopItemRepository.save(shopItem);
    }
}
