package com.mirae.DailyBoost.oauth.business;

import com.mirae.DailyBoost.oauth.OAuthAttributes;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.business.UserBusiness;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserBusiness implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserBusiness userBusiness;
  private final HttpSession httpSession;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    // 현재 로그인 진행 중인 서비스를 구분하는 코드
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    log.info("==============={}=========", registrationId);

    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
        oAuth2User.getAttributes());

    String email = attributes.getEmail();
    log.info("=========={}=========", email);

    User user = userBusiness.getByEmailElseRegister(email, attributes);

    if(user.getStatus() == UserStatus.UNREGISTERED) {
      throw new IllegalAccessError("회원님의 계정은 탈퇴되어 한달 후 영구 삭제될 예정입니다."
          + "로그인을 하려면 탈퇴 취소를 해주세요.");
    }

    if(user.getStatus() == UserStatus.DORMANT) {
      throw new IllegalAccessError("회원님의 계정이 휴면계정이 되었습니다."
          + "로그인을 하려면 계정 복구를 해주세요.");
    }

    log.info("세션 저장 시작 -> {}", user.getEmail());
    httpSession.setAttribute("user", new UserDTO(user));
    log.info("세션 저장 완료 -> {}", httpSession.getAttribute("user"));

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
        attributes.getAttributeMap(),
        attributes.getNameAttributeKey());

  }
}
