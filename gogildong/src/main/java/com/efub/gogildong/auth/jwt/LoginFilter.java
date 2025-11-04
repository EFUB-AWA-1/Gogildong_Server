package com.efub.gogildong.auth.jwt;

import com.efub.gogildong.auth.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/login"); // 로그인 URL
        setAuthenticationManager(authenticationManager);

        setUsernameParameter("loginId");
        setPasswordParameter("password");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String loginId, password;
        try {
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                var node = new ObjectMapper().readTree(request.getInputStream());
                loginId = node.get("loginId").asText();
                password = node.get("password").asText();
            } else {
                loginId = obtainUsername(request);
                password = obtainPassword(request);
            }
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid login payload", e);
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginId, password)
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String username = principal.getUsername(); // 내부적으로 loginId 반환하도록 구현되어 있어야 함

        String domainRole = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)            // ex) ROLE_ADMIN
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a) // -> ADMIN
                .findFirst().orElse("EXTERNAL");

        String accessToken  = jwtUtil.createAccessToken(username, domainRole);
        String refreshToken = jwtUtil.createRefreshToken(username);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("X-Refresh-Token", refreshToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization,X-Refresh-Token");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
            {"accessToken":"%s","refreshToken":"%s"}
        """.formatted(accessToken, refreshToken));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"invalid_credentials\"}");
        response.getWriter().flush();
    }
}


