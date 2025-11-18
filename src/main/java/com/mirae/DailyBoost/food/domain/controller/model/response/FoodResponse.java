package com.mirae.DailyBoost.food.domain.controller.model.response;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class FoodResponse {

  private Long id;
  private String name;
  private BigDecimal calory;
  private BigDecimal carbohydrate;
  private BigDecimal protein;
  private BigDecimal fat;
  private FoodKind foodKind;
  private String description;
  private Long weight;

}
