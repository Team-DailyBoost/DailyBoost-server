package com.mirae.DailyBoost.oauth.business;

import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.oauth.CustomOAuth2User;
import com.mirae.DailyBoost.oauth.OAuthAttributes;
import com.mirae.DailyBoost.user.domain.business.UserBusiness;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import com.mirae.DailyBoost.user.exception.user.AlreadyUnregisteredException;
import com.mirae.DailyBoost.user.exception.user.DormantAccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserBusiness implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserBusiness userBusiness;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    // 현재 로그인 진행 중인 서비스를 구분하는 코드 naver, kakao, google
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    log.info("==============={}=========", registrationId);

    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
        oAuth2User.getAttributes());

    String email = attributes.getEmail();
    log.info("=========={}=========", email);
    log.info("=========={}=========", attributes.getName());

    // 등록되지 않은 이메일일 때, 회원 등록
    User user = userBusiness.getByEmailElseRegister(email, attributes);

    if(user.getStatus() == UserStatus.UNREGISTERED) {
      throw new AlreadyUnregisteredException(UserErrorCode.ALREADY_UNREGISTERED);
    }

    if(user.getStatus() == UserStatus.DORMANT) {
      throw new DormantAccountException(UserErrorCode.DORMANT_ACCOUNT);
    }

    return new CustomOAuth2User(user, attributes.getAttributeMap());
  }
}
