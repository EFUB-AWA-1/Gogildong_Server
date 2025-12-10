package com.efub.gogildong.auth.service;

import com.efub.gogildong.auth.dto.TokenResponse;
import com.efub.gogildong.auth.jwt.JwtUtil;
import com.efub.gogildong.auth.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenResponse reissueTokens(String refreshToken) {

        // 기본 파싱/유효성 검사
        try {
            // parseClaims 안에서 서명 검증/만료 체크 중 예외 발생 가능
            jwtUtil.parseClaims(refreshToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 토큰임", e);
        }

        // 타입 체크
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("refresh 토큰이 아님");
        }

        // 만료 체크
        if (jwtUtil.isExpired(refreshToken)) {
            throw new IllegalArgumentException("refresh 토큰이 만료됨");
        }

        // subject = username(loginId) 꺼내기
        String username = jwtUtil.getSubject(refreshToken);

        // DB에서 유저 다시 로드 → role 가져오기
        CustomUserDetails userDetails =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없음");
        }

        String domainRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)            // ex) ROLE_ADMIN
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a) // ADMIN
                .findFirst()
                .orElse("EXTERNAL");

        // 새 토큰 발급
        String newAccessToken  = jwtUtil.createAccessToken(username, domainRole);
        String newRefreshToken = jwtUtil.createRefreshToken(username);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
