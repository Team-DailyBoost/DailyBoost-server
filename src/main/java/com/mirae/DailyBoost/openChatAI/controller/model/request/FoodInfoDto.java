package com.mirae.DailyBoost.openChatAI.controller.model.request;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodInfoDto {

  private String name;
  private BigDecimal calory;
  private BigDecimal carbohydrate;
  private BigDecimal protein;
  private BigDecimal fat;
  private FoodKind foodKind;
  private String description;

}
