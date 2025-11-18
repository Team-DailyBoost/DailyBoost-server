package com.mirae.DailyBoost.food.domain.converter;

import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodRecommendation;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.util.List;

@Converter
public class FoodConverter {

  public List<Food> toEntity(User user, List<FoodRecommendation> foodsRecommendation) {
    return foodsRecommendation.stream().map(foodRecommendation -> {
      return Food.builder()
          .name(foodRecommendation.getName())
          .calory(foodRecommendation.getCalory())
          .carbohydrate(foodRecommendation.getCarbohydrate())
          .protein(foodRecommendation.getProtein())
          .fat(foodRecommendation.getFat())
          .foodKind(foodRecommendation.getFoodKind())
          .description(foodRecommendation.getDescription())
          .status(FoodStatus.UNREGISTERED)
          .weight(foodRecommendation.getWeight())
          .user(user)
          .build();
    }).toList();
  }

  public Food toRecipeFoodEntity(User user, FoodRecommendation foodRecommendation) {
    return Food.builder()
        .name(foodRecommendation.getName())
        .calory(foodRecommendation.getCalory())
        .carbohydrate(foodRecommendation.getCarbohydrate())
        .protein(foodRecommendation.getProtein())
        .fat(foodRecommendation.getFat())
        .foodKind(foodRecommendation.getFoodKind())
        .description(foodRecommendation.getDescription())
        .status(FoodStatus.UNREGISTERED)
        .weight(foodRecommendation.getWeight())
        .user(user)
        .build();
  }

  public List<FoodResponse> toResponse(List<Food> foods) {
    return foods.stream().map(food -> {
      return FoodResponse.builder()
          .id(food.getId())
          .name(food.getName())
          .calory(food.getCalory())
          .carbohydrate(food.getCarbohydrate())
          .protein(food.getProtein())
          .fat(food.getFat())
          .foodKind(food.getFoodKind())
          .description(food.getDescription())
          .weight(food.getWeight())
          .build();
    }).toList();
  }
}
