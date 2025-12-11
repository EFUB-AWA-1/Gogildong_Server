package com.efub.gogildong.shops.controller;

import com.efub.gogildong.shops.dto.response.CoinResponse;
import com.efub.gogildong.shops.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coin")
public class CoinController {

    private final CoinService coinService;

    /*
    * 사용자가 보유한 코인 조회
    * */
    @GetMapping("/me")
    public ResponseEntity<CoinResponse> getMyCoin(Authentication authentication) {
        return ResponseEntity.ok(coinService.getMyCoin(authentication.getName()));
    }
}
