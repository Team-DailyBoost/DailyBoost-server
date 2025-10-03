package com.mirae.DailyBoost.user.domain.controller.model.request;

import com.mirae.DailyBoost.user.domain.repository.HealthInfo;
import com.mirae.DailyBoost.user.domain.repository.enums.Gender;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
  private String age;
  private Gender gender;
  private HealthInfo healthInfo;
}
