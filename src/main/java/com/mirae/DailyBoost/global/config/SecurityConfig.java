package com.mirae.DailyBoost.global.config;

import com.mirae.DailyBoost.jwt.business.JwtBusiness;
import com.mirae.DailyBoost.jwt.filter.JwtAuthenticationProcessingFilter;
import com.mirae.DailyBoost.oauth.business.CustomOAuth2UserBusiness;
import com.mirae.DailyBoost.oauth.handler.CustomLoginFailureHandler;
import com.mirae.DailyBoost.oauth.handler.CustomLoginSuccessHandler;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserBusiness customOAuth2UserBusiness;
  private final CustomLoginSuccessHandler customLoginSuccessHandler;
  private final CustomLoginFailureHandler customLoginFailureHandler;
  private final JwtBusiness jwtBusiness;
  private final UserService userService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.cors(c -> c.configurationSource(req -> {
      var cfg = new CorsConfiguration();
      cfg.setAllowedOrigins(List.of("http://localhost:3000"));
      cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
      cfg.setAllowedHeaders(List.of("*"));
      cfg.setExposedHeaders(List.of("Authorization","X-Refresh-Token"));
      cfg.setAllowCredentials(true); // JWT를 헤더로 사용하더라도 true로 설정
      cfg.setMaxAge(3600L);
      return cfg;
    }));

    http.csrf(csrf ->
        csrf.ignoringRequestMatchers("/api/**"));

    // 해당 URL 패턴들은 로그인 없이 모든 사용자가 접근 가능
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/email/htmlEmail").permitAll()
            .requestMatchers("/api/user/recover").permitAll()
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**","/swagger-ui.html").permitAll()
            .requestMatchers("/", "/css/**", "/js/**", "/login").permitAll()
            .requestMatchers("/api/**").hasAnyAuthority(Role.USER.getStr(), Role.ADMIN.getStr()) // ROLE_USER, ROLE_ADMIN 권한 확인
            .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 가능
        );

    http.formLogin((auth) -> auth.disable());
    http.httpBasic((auth ->  auth.disable()));

    http.oauth2Login(auth -> auth
        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserBusiness))
        .successHandler(customLoginSuccessHandler) // OAuth2 로그인 성공 후 핸들러 호출
        .failureHandler(customLoginFailureHandler)
        .failureUrl("/login?error=true")
    );

    http.logout(logout -> logout.logoutSuccessUrl("/")); // 로그아웃 시 리다이렉트 될 URL을 설정

    http.sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
    http.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtBusiness, userService);
  }

}
