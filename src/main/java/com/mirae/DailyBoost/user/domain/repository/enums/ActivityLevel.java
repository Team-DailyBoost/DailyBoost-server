package com.mirae.DailyBoost.user.domain.repository.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ActivityLevel {
  SEDENTARY("거의 활동하지 않음"),
  LIGHT("가벼운 활동"),
  MODERATE("보통 활동"),
  ACTIVE("활동적"),
  VERY_ACTIVE("매우 활동적");

  private String description;
}
