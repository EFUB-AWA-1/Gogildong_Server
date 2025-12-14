package com.efub.gogildong.global.config;

import com.efub.gogildong.auth.jwt.JwtFilter;
import com.efub.gogildong.auth.jwt.JwtUtil;
import com.efub.gogildong.auth.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter filter = new LoginFilter(authenticationManager, jwtUtil);
        filter.setFilterProcessesUrl("/auth/login");
        filter.setUsernameParameter("loginId");
        filter.setPasswordParameter("password");
        return filter;
    }

    // cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",       // 로컬 개발용
                "https://localhost:5173",
                "https://gogildong.vercel.app",     // 배포 프론트
                "https://192.168.35.42:5173",
                "https://192.168.35.185:5173"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        // 프론트에서 읽어야 하는 헤더
        config.setExposedHeaders(List.of("Authorization", "X-Refresh-Token"));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {}) // 위에서 정의한 corsConfigurationSource() 사용
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/login", "/auth/refresh", "/users/signup/**", "/auth/email/**").permitAll()
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/me").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler())
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setHeader("Authorization", "");
                    res.setHeader("X-Refresh-Token", "");
                    res.addHeader("Set-Cookie", "AccessToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
                    res.addHeader("Set-Cookie", "RefreshToken=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
                    res.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
                })
        );

        return http.build();
    }

    @Bean
    public LogoutHandler logoutHandler() {
        // 필요 시 여기서 refresh 토큰 블랙리스트/폐기 로직 추가
        return new SecurityContextLogoutHandler();
    }
}
