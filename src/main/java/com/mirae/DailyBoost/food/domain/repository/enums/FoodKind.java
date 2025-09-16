package com.mirae.DailyBoost.food.domain.repository.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FoodKind {

  BREAKFAST("아침"),
  LUNCH("점심"),
  DINNER("저녁"),
  RECIPE("레시피");

  String description;
}
