package com.mirae.DailyBoost.oauth.resolver;

import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.oauth.CustomOAuth2User;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {


  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
    boolean isUserClass = UserDTO.class.equals(parameter.getParameterType());

    return isLoginUserAnnotation && isUserClass;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication == null) {
      log.warn("인증 정보가 없습니다.");
      throw new IllegalArgumentException("인증 정보가 없습니다.");
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof CustomOAuth2User customOAuth2User) {
      User user = customOAuth2User.getUser();
      return new UserDTO(user);
    }

    return null;
  }
}
