package com.mirae.DailyBoost.food.domain.business;

import com.mirae.DailyBoost.common.annotation.Business;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.converter.MessageConverter;
import com.mirae.DailyBoost.common.moel.MessageResponse;
import com.mirae.DailyBoost.food.domain.Controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.converter.FoodConverter;
import com.mirae.DailyBoost.food.domain.repository.Food;
import com.mirae.DailyBoost.food.domain.repository.enums.FoodKind;
import com.mirae.DailyBoost.food.domain.service.FoodService;
import com.mirae.DailyBoost.openChatAI.controller.model.request.FoodRecommendation;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class FoodBusiness {

  private final FoodService foodService;
  private final FoodConverter foodConverter;
  private final MessageConverter messageConverter;

  public MessageResponse register(FoodRecommendation foodRecommendation) {

    // userId 추가 해야 함.
    List<Food> foods = foodConverter.toEntity(foodRecommendation);
    FoodKind foodKind = foods.stream().map(Food::getFoodKind).findFirst().orElse(null);

    foods.forEach(food -> foodService.register(food));

    if (foodKind == FoodKind.RECIPE) {
      return messageConverter.toResponse("레시피가 등록되었습니다.");
    } else {
      return messageConverter.toResponse("식단이 등록되었습니다.");
    }
  }

  public FoodResponse getByKind(Long id, FoodKind foodKind) {

    Food food = foodService.getByIdAndKind(id, foodKind).orElseThrow(
        () -> new IllegalArgumentException());

    return foodConverter.toResponse(food);
  }

  public FoodResponse getByName(Long id, String name) {
    Food food = foodService.getByIdAndName(id, name).orElseThrow(
        () -> new IllegalArgumentException());

    return foodConverter.toResponse(food);

  }

}
