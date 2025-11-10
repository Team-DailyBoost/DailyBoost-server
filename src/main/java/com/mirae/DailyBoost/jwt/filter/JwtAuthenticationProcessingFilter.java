package com.mirae.DailyBoost.jwt.filter;

import com.mirae.DailyBoost.jwt.business.JwtBusiness;
import com.mirae.DailyBoost.oauth.CustomOAuth2User;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URL 요청이 왔을 때, 처리하는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

  // "/login"으로 들어오는 요청은 Filter 작동 X
  private static final String NO_CHECK_URL = "/login";

  private final JwtBusiness jwtBusiness;
  private final UserService userService;

  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("JWT Filter 실행 - URI: {}", request.getRequestURI());

    // 이미 세션/컨텍스트에 인증이 있으면 JWT 검사 생략
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }
    
    if (request.getRequestURI().equals(NO_CHECK_URL)) {
      filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
      return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
    }

    // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청한 경우밖에 없다.
    String refreshToken = jwtBusiness.extractRefreshToken(request) // 사용자 헤더에서 RefreshToken 추출
        .filter(jwtBusiness::isTokenValid) // 토큰 유효성 검사
        .orElse(null); // RefreshToken이 없거나 유효하지 않으면 null 반환.

    // 요청 헤더에 RefreshToken 존재 -> 사용자의 AccessToken 만료
    if (refreshToken != null) {
      log.info("RefreshToken 발견, AccessToken 재발급 시도");
      checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
      return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
    }

    if (refreshToken == null) {
      log.info("AccessToken으로 인증 시도");
      checkAccessTokenAndAuthentication(request, response, filterChain);
    }
  }

  public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
    userService.getByRefreshToken(refreshToken) // refreshToken으로 유저 정보 찾기
        .ifPresent(user -> {
          String reIssuedRefreshToken = reIssueRefreshToken(user); // refreshToken 재발급, DB refreshToken 업데이트
          jwtBusiness.sendAccessAndRefreshToken(response, jwtBusiness.createAccessToken(user.getEmail(), user.getNickname(),
                  user.getRole()), reIssuedRefreshToken); // 재발급한 refreshToken 헤더에 전송
        });
  }

  private String reIssueRefreshToken(User user) {
    String reIssuedRefreshToken = jwtBusiness.createRefreshToken(); // refreshToken 재발급
    user.updateRefreshToken(reIssuedRefreshToken); // 유저 refreshToken 재설정
    userService.save(user); // 유저 정보 저장
    return reIssuedRefreshToken; // 재발급한 refreshToken 반환
  }

  /**
   * [액세스 토큰 체크 & 인증 처리 메소드]
   * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
   * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
   * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
   * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
   * 그 후 다음 인증 필터로 진행
   */
  public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("checkAccessTokenAndAuthentication() 호출");
    jwtBusiness.extractAccessToken(request) // 헤더에서 accessToken 추출
        .filter(jwtBusiness::isTokenValid) // 토큰 유효성 검사
        .ifPresent(accessToken -> {
          log.info("유효한 AccessToken 발견: {}", accessToken);
          jwtBusiness.extractEmail(accessToken) // email 추출
              .ifPresent(email -> {
                log.info("토큰에서 추출한 이메일: {}", email);
                userService.getByEmail(email) // email로 유저 조회
                    .ifPresent(user -> {
                      log.info("유저 조회 성공: {}, 권한: {}", user.getEmail(), user.getRole());
                      saveAuthentication(user);
                    });
              });
        });

    filterChain.doFilter(request, response);
  }

  public void saveAuthentication(User user) {
    log.info("saveAuthentication() 호출 - 사용자: {}, 권한: {}", user.getEmail(), user.getRole().getStr());
    CustomOAuth2User customOAuth2User = CustomOAuth2User.fromUser(user);

    Collection<? extends GrantedAuthority> auths = customOAuth2User.getAuthorities();
    log.info("GrantedAuthorities: {}", auths);
    
    if (authoritiesMapper != null) { // 선택적 매퍼 사용
      auths = authoritiesMapper.mapAuthorities(auths);
    }

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(customOAuth2User, null,
            authoritiesMapper.mapAuthorities(customOAuth2User.getAuthorities()));

    // 세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.info("SecurityContext에 인증 정보 저장 완료");
  }
}