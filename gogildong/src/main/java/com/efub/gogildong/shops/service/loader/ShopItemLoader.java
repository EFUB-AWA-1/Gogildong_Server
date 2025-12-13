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
        insertIfNotExists("기본 얼굴", "/head/default-head.svg", "/head/default-head.svg", ClotheType.HEAD, 0, true);
        insertIfNotExists("길동이 옷", "/body/default-body.svg", "/body/default-body.svg", ClotheType.DRESS, 0, true);


        // --- 일반 상점 아이템 ---
        insertIfNotExists("검정 머리", "/head/black.svg", "/head/black-hat.svg", ClotheType.HEAD, 30, false);
        insertIfNotExists("갈색 머리", "/head/brown.svg", "/head/brown-hat.svg", ClotheType.HEAD, 35, false);
        insertIfNotExists("캡모자", "/head/cap.svg", "/head/cap-hat.svg", ClotheType.HEAD, 30, false);
        insertIfNotExists("삐에로 모자", "/head/clown.svg", "/head/clown-hat.svg", ClotheType.HEAD, 40, false);
        insertIfNotExists("판다 모자", "/head/panda.svg", "/head/panda-hat.svg", ClotheType.HEAD, 50, false);
        insertIfNotExists("분홍 머리", "/head/pink.svg", "/head/pink-hat.svg", ClotheType.HEAD, 35, false);
        insertIfNotExists("상투", "/head/sangtoo.svg", "/head/sangtoo-hat.svg", ClotheType.HEAD, 20, false);
        insertIfNotExists("산타 모자", "/head/santa.svg", "/head/santa-hat.svg", ClotheType.HEAD, 42, false);

        insertIfNotExists("삐에로 옷", "/body/clown-cloth.svg", "/body/clown-cloth.svg", ClotheType.DRESS, 42, false);
        insertIfNotExists("곤룡포", "/body/king.svg", "/body/king.svg", ClotheType.DRESS, 100, false);
        insertIfNotExists("I♥길동", "/body/love.svg", "/body/love.svg", ClotheType.DRESS, 32, false);
        insertIfNotExists("산타 옷", "/body/santa-cloth.svg", "/body/santa-cloth.svg", ClotheType.DRESS, 42, false);
        insertIfNotExists("트레이닝복", "/body/train.svg", "/body/train.svg", ClotheType.DRESS, 30, false);
    }

    private void insertIfNotExists(String name, String itemfileName, String wearingItemfileName, ClotheType type, int price, boolean defaultFlag){
        if(shopItemRepository.existsByName(name)){
            return;
        }
        ShopItem shopItem = ShopItem.builder()
                .name(name)
                .type(type)
                .price(price)
                .defaultFlag(defaultFlag)
                .itemImage(baseImageUrl + itemfileName)
                .wearingItemImage(baseImageUrl + wearingItemfileName)
                .build();
        shopItemRepository.save(shopItem);
    }
}
