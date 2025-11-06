package com.mirae.DailyBoost.oauth;

import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

  private User user;
  private Map<String, Object> attributeMap;

  @Override
  public Map<String, Object> getAttributes() {
    return attributeMap == null ? Collections.emptyMap() : Collections.unmodifiableMap(attributeMap);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> this.user.getRole().getStr());
  }

  @Override
  public String getName() {
    return this.user.getNickname();
  }

  public String getEmail() {
    return this.user.getEmail();
  }

  // JWT 경로에서 속성맵이 없을 때
  public static CustomOAuth2User fromUser(User user) {
    return new CustomOAuth2User(user, Collections.emptyMap());
  }

}
