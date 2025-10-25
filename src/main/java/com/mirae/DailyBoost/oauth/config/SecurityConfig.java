package com.mirae.DailyBoost.oauth.config;

import com.mirae.DailyBoost.oauth.business.CustomOAuth2UserBusiness;
import com.mirae.DailyBoost.oauth.handler.CustomLoginSuccessHandler;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserBusiness customOAuth2UserBusiness;
  private final CustomLoginSuccessHandler customLoginSuccessHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/**"))
        .authorizeHttpRequests(auth -> auth // 수정 예정
            .requestMatchers("/api/email/htmlEmail").permitAll()
            .requestMatchers("/api/user/recover").permitAll()
            .requestMatchers("/", "/css/**", "/js/**", "/login").permitAll() // 해당 URL 패턴들은 모든 사용자가 접근 가능
            .requestMatchers("/api/**").hasRole(Role.USER.name()) // "/api/**" 패턴의 URL은 USER 권한을 가진 사용자만 접근 가능
            .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 가능
        )
        // 커스텀 폼 로그인 설정
//        .formLogin(form -> form
//            .loginPage("/login")
//        ) 요청 URL에 따른 권한을 설정
        .logout(logout -> logout.logoutSuccessUrl("/")) // 로그아웃 시 리다이렉트 될 URL을 설정
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo ->
                userInfo.userService(customOAuth2UserBusiness))// OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
            .successHandler(customLoginSuccessHandler)
            .failureUrl("/login?error=true")
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/") // 로그아웃 후 이동
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        );
    return http.build();
  }

}
