package com.mirae.DailyBoost.oauth.resolver;

import com.mirae.DailyBoost.common.annotation.LoginUser;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
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

  // 파라미터에 전달한 객체를 생성
  // 세션에서 객체를 가져온다.
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    // 이미 세션이 있다면 그 세션을 돌려주고, 세션이 없으면 null을 돌려준다.
    HttpSession session = request.getSession(false); // 현재 요청에 연결된 세션

    if (session == null) {
      log.warn("세션이 없습니다.");
      return null;
    }

    return session.getAttribute("user");
  }
}
