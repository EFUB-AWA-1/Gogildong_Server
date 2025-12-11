package com.efub.gogildong.shops.service;

import com.efub.gogildong.global.exception.ExceptionCode;
import com.efub.gogildong.global.exception.GoGildongException;
import com.efub.gogildong.global.util.EntityFinder;
import com.efub.gogildong.shops.dto.response.CoinResponse;
import com.efub.gogildong.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final EntityFinder finder;

    @Transactional(readOnly = true)
    public CoinResponse getMyCoin(String loginId){
        User user = finder.getUserByLoginId(loginId);
        return new CoinResponse(user.getCoin());
    }

    @Transactional
    public void earnCoin(User user, int amount) {
        int before = user.getCoin();
        user.setCoin(before + amount);
    }

    @Transactional
    public void spendCoin(User user, int amount) {
        int before = user.getCoin();
        int after = user.getCoin() - amount;
        if(after < 0) {
            throw new GoGildongException(ExceptionCode.COIN_INSUFFICIENT_BALANCE);
        }
        user.setCoin(before - amount);
    }
}
