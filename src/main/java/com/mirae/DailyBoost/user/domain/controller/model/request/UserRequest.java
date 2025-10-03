package com.mirae.DailyBoost.user.domain.controller.model.request;

import com.mirae.DailyBoost.user.domain.repository.HealthInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
  private HealthInfo healthInfo;

}
