package com.mirae.DailyBoost.oauth.handler;

import com.mirae.DailyBoost.food.domain.business.FoodBusiness;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.UserRepository;
import com.mirae.DailyBoost.user.exception.user.HealthInfoNotSetException;
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

  private final UserRepository userRepository;
  private final FoodBusiness foodBusiness;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

    String email = userDTO.getEmail();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    user.initLastLoginAt(LocalDateTime.now());
    log.info("로그인 성공 {}", email);

    userRepository.save(user);

    // 수정
    response.sendRedirect("/main.html"); // 로그인 직후 /main 페이지로 이동
    super.onAuthenticationSuccess(request, response, authentication);

    // 식단 추천 (생각 좀 해야 함.)
//    if(user.getHealthInfo() == null) {
//      throw new HealthInfoNotSetException(UserErrorCode.HEALTH_INFO_NOT_SET);
//    } // -> healthInfo 설정 API
//    foodBusiness.recommendFood(userDTO);
  }
}
