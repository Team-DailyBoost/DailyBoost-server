package com.mirae.DailyBoost.oauth.handler;

import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.UserRepository;
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

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

    String email = userDTO.getEmail();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    user.initLastLoginAt(LocalDateTime.now());
    log.info("로그인 성공 {}", email);

    userRepository.save(user);

    response.sendRedirect("/main.html"); // 로그인 직후 /main 페이지로 이동
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
