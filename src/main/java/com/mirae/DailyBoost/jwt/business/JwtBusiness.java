package com.mirae.DailyBoost.jwt.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import com.mirae.DailyBoost.user.domain.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Business
@Getter
@RequiredArgsConstructor
public class JwtBusiness {

  private final SecretKey secretKey;

  @Value("${spring.jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${spring.jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${spring.jwt.access.header}")
  private String accessHeader;

  @Value("${spring.jwt.refresh.header}")
  private String refreshHeader;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String BEARER = "Bearer ";

  private final UserService userService;

  /**
   * AccessToken 생성 메서드
   * @param email
   * @param nickname
   * @param role
   * @return Jwts
   */
  public String createAccessToken(String email, String nickname, Role role) {
    long now = System.currentTimeMillis();
    return Jwts.builder() // JWT 토큰을 생성하는 빌더 반환
        .subject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken
        .issuedAt(new Date(now)) // 발행 시간
        .expiration(new Date(now + accessTokenExpirationPeriod)) // 토큰 만료 시간
        .claim(EMAIL_CLAIM, email)
        .claim("nickname", nickname)
        .claim("role", role.name())
        .signWith(secretKey, Jwts.SIG.HS512)
        .compact();
  }

  /**
   * refreshToken 생성 메서드
   * @return Jwts
   */
  public String createRefreshToken() {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .subject(REFRESH_TOKEN_SUBJECT)
        .issuedAt(new Date(now))
        .expiration(new Date(now + refreshTokenExpirationPeriod))
        .signWith(secretKey, Jwts.SIG.HS512)
        .compact();
  }

  /**
   * AccessToken 헤더에 실어서 보내기
   * @param response
   * @param accessToken
   */
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK); // OK
    response.setHeader(accessHeader, BEARER + accessToken); // header에 accessToken 실어보내기
    log.info("재발급된 Access Token : {}", accessToken);
  }

  /**
   * AccessToken + RefreshToken 헤더에 실어서 보내기
   * @param response
   * @param accessToken
   * @param refreshToken
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);

    response.setHeader(accessHeader, BEARER + accessToken);
    response.setHeader(refreshHeader, refreshToken);
    log.info("Access Token, Refresh Token 헤더 설정 완료");
  }

  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader)) // refreshHeader 가져오기 요청
        .filter(refreshToken -> refreshToken.startsWith(BEARER)) // filter BEARER로 시작하는 헤더로 걸러서
        .map(refreshToken -> refreshToken.replace(BEARER, "")); // 제거 후 반환
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(accessToken -> accessToken.startsWith(BEARER))
        .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  public Optional<String> extractEmail(String accessToken) {
    try {
      if (accessToken == null || accessToken.isBlank()) return Optional.empty();
      Claims claims = Jwts.parser()
          .verifyWith(secretKey) // 토큰 유효성 검사
          .build()
          .parseSignedClaims(accessToken)
          .getPayload();

      return Optional.ofNullable(claims.get(EMAIL_CLAIM, String.class)); // 유저 이메일 반환
    } catch (JwtException | IllegalStateException e ) {
      log.error("엑세스 토큰이 유효하지 않습니다.");
      return Optional.empty();
    }
  }

  public void updateRefreshToken(String email, String refreshToken) {
    userService.getByEmail(email)
        .ifPresentOrElse(
            user -> user.updateRefreshToken(refreshToken),
            () -> new IllegalArgumentException("일치하는 회원이 없습니다.")
        );
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);

      return true;
    } catch (JwtException | IllegalStateException e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }

}
