package com.efub.gogildong.auth.controller;

import com.efub.gogildong.auth.dto.RefreshTokenRequest;
import com.efub.gogildong.auth.dto.TokenResponse;
import com.efub.gogildong.auth.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthTokenService authTokenService;

    @PostMapping("/refresh")
    public void reissue(@RequestBody RefreshTokenRequest request,
                        HttpServletResponse response) throws IOException {

        TokenResponse tokenResponse = authTokenService.reissueTokens(request.getRefreshToken());

        // 헤더 + 바디 둘 다 내려줌
        response.setHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());
        response.setHeader("X-Refresh-Token", tokenResponse.getRefreshToken());
        response.setHeader("Access-Control-Expose-Headers", "Authorization,X-Refresh-Token");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
            {"accessToken":"%s","refreshToken":"%s"}
        """.formatted(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken()));
        response.getWriter().flush();
    }
}
