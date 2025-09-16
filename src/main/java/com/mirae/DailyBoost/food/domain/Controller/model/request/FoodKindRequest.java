package com.mirae.DailyBoost.food.domain.Controller.model.request;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FoodKindRequest {
  FoodKind foodKind;

}
