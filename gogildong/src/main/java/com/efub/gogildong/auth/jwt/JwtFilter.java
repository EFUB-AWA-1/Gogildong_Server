package com.efub.gogildong.auth.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        // 만료/형식/서명은 여기서 걸러냄(만료 시 401로 내보내고 싶다면 EntryPoint에서 처리)
        try {
            if (jwtUtil.isExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (JwtException e) {
            filterChain.doFilter(request, response);
            return;
        }

        // refresh 토큰이 헤더로 오면 거부
        String typ = jwtUtil.getType(token);
        if ("refresh".equalsIgnoreCase(typ)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 subject/role 추출 (role은 도메인 롤)
        String username = jwtUtil.getSubject(token);
        String domainRole = jwtUtil.getRole(token);
        String authority = "ROLE_" + domainRole;

        // DB hit 없이 경량 UserDetails 생성
        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("")
                .authorities(authority)
                .build();

        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
