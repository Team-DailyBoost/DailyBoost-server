package com.mirae.DailyBoost.oauth;

import com.mirae.DailyBoost.user.domain.repository.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

  private User user;
  private Map<String, Object> attributeMap;

  @Override
  public Map<String, Object> getAttributes() {
    return this.attributeMap;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> this.user.getRole().getStr());
  }

  @Override
  public String getName() {
    return this.user.getNickname();
  }

  public User getUser() {
    return this.user;
  }
}
