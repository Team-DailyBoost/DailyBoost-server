package com.mirae.DailyBoost.user.domain.converter;

import com.mirae.DailyBoost.common.annotation.Converter;
import com.mirae.DailyBoost.oauth.OAuthAttributes;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import java.time.LocalDateTime;

@Converter
public class UserConverter {

  public User toEntity(OAuthAttributes attributes) {
    return User.builder()
        .email(attributes.getEmail())
        .name(attributes.getName())
        .nickname(attributes.getNickname())
        .phone(attributes.getPhone())
        .gender(attributes.getGender())
        .registered_at(LocalDateTime.now())
        .status(UserStatus.REGISTERED)
        .role(Role.USER)
        .birthDay(attributes.getBirthDay())
        .provider(attributes.getProvider())
        .providerId(attributes.getProviderId())
        .build();
  }

}
