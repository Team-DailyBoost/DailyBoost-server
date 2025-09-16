package com.mirae.DailyBoost.food.domain.converter;

import com.mirae.DailyBoost.common.annotation.Converter;
import com.mirae.DailyBoost.food.domain.Controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.openChatAI.controller.model.request.FoodRecommendation;
import java.util.List;

@Converter
public class FoodConverter {

  public List<Food> toEntity(FoodRecommendation foodRecommendation) {
    return foodRecommendation.getFoodInfoDto().stream().map(foodInfoDto -> {
      return Food.builder()
          .name(foodInfoDto.getName())
          .calory(foodInfoDto.getCalory())
          .carbohydrate(foodInfoDto.getCarbohydrate())
          .protein(foodInfoDto.getProtein())
          .fat(foodInfoDto.getFat())
          .foodKind(foodInfoDto.getFoodKind())
          .description(foodInfoDto.getDescription())
          .build();
    }).toList();
  }

  public FoodResponse toResponse(Food food) {
    return FoodResponse.builder()
        .name(food.getName())
        .calory(food.getCalory())
        .carbohydrate(food.getCarbohydrate())
        .protein(food.getProtein())
        .fat(food.getFat())
        .foodKind(food.getFoodKind())
        .description(food.getDescription())
        .build();
  }
}
