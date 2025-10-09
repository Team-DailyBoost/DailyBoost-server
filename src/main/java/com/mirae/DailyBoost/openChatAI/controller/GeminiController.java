package com.mirae.DailyBoost.openChatAI.controller;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.openChatAI.business.GeminiBusiness;
import com.mirae.DailyBoost.openChatAI.controller.model.request.ExerciseRequest;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodRecommendation;
import com.mirae.DailyBoost.openChatAI.controller.model.request.FoodRequest;
import com.mirae.DailyBoost.food.domain.controller.model.request.RecipeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class GeminiController {
  private final GeminiBusiness geminiBusiness;

  @GetMapping("/recommend/exercise")
  public Api<ExerciseRecommendation> recommendExercise(@RequestBody ExerciseRequest exerciseRequest) {
    return Api.OK(geminiBusiness.recommendExercise(exerciseRequest));

  }

  @GetMapping("/recommend/food")
  public Api<FoodRecommendation> recommendFood(@RequestBody FoodRequest foodRequest) {
    return Api.OK(geminiBusiness.recommendFood(foodRequest));

  }


  @GetMapping("/recommend/recipe")
  public Api<FoodRecommendation> recommendRecipe(@RequestBody RecipeRequest recipeRequest) {
    return Api.OK(geminiBusiness.recommendRecipe(recipeRequest));

  }

}