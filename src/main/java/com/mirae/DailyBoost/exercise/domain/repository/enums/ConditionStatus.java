package com.mirae.DailyBoost.exercise.domain.repository.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionStatus {

  TIRED("피곤함"),
  NORMAL("보통"),
  BEST( "최상");

  private final String description;
}
