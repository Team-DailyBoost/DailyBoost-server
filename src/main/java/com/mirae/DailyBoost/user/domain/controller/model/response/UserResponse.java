package com.mirae.DailyBoost.user.domain.controller.model.response;


import com.mirae.DailyBoost.user.domain.repository.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long id;
  private String email;
  private String name;
  private String phone;
  private String nickname;
  private Gender gender;

}
