package com.mirae.DailyBoost.oauth.dto;

import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDTO {
  private Long id;
  private String email;
  private Role role;
  private String provider;
  private String providerId;

  public UserDTO(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.provider = user.getProvider();
    this.providerId = user.getProviderId();
  }

}
