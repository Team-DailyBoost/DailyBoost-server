package com.mirae.DailyBoost.oauth.handler;

import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.jwt.business.JwtBusiness;
import com.mirae.DailyBoost.oauth.CustomOAuth2User;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtBusiness jwtBusiness;
  private final UserService userService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

      String accessToken = jwtBusiness.createAccessToken(oAuth2User.getEmail(), oAuth2User.getName(), oAuth2User.getUser()
          .getRole());
      String refreshToken = jwtBusiness.createRefreshToken();
      response.addHeader(jwtBusiness.getAccessHeader(), "Bearer " + accessToken);
      response.addHeader(jwtBusiness.getRefreshHeader(), "Bearer " + refreshToken);

      jwtBusiness.sendAccessAndRefreshToken(response, accessToken, refreshToken);
      jwtBusiness.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

      User user = userService.getByEmail(oAuth2User.getEmail())
          .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
      user.initLastLoginAt(LocalDateTime.now());
      log.info("로그인 성공 =================== {} ================", oAuth2User.getEmail());
      userService.save(user);

    } catch (Exception e) {
      throw e;
    }

  }

}
