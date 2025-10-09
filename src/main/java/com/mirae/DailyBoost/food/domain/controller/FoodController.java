package com.mirae.DailyBoost.food.domain.controller;

import com.mirae.DailyBoost.common.annotation.LoginUser;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.food.domain.controller.model.request.FoodKindRequest;
import com.mirae.DailyBoost.food.domain.controller.model.request.FoodNameRequest;
import com.mirae.DailyBoost.food.domain.controller.model.request.RecipeRequest;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.business.FoodBusiness;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.food.domain.controller.model.response.FoodRecommendation;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

  private final FoodBusiness foodBusiness;

  @PostMapping("/register/{foodId}")
  @Operation(
      summary = "식단 기록에 추가",
      description = "추천 받은 식단을 기록에 추가합니다."
  )
  public Api<MessageResponse> register(@LoginUser UserDTO userDTO, @PathVariable Long foodId) {
    return Api.OK(foodBusiness.register(userDTO, foodId));
  }

  @PostMapping("/unregister/{foodId}")
  @Operation(
      summary = "식단 기록에서 제거",
      description = "기록에서 식단을 제거합니다."
  )
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @PathVariable Long foodId) {
    return Api.OK(foodBusiness.unregister(userDTO, foodId));
  }

  @PostMapping("/reset")
  @Operation(
      summary = "일일 식단 초기화",
      description = "일일 식단 기록를 초기화합니다."
  )
  public Api<MessageResponse> reset(@LoginUser UserDTO userDTO) {
    return Api.OK(foodBusiness.reset(userDTO));
  }

  @GetMapping("/recipe/recommend")
  @Operation(
      summary = "레시피 추천",
      description = "LLM으로부터 레시피를 추천받습니다."
  )
  public Api<FoodRecommendation> recommendRecipe(@LoginUser UserDTO userDTO, @RequestBody RecipeRequest recipeRequest) {
    return Api.OK(foodBusiness.recommendRecipe(userDTO, recipeRequest));
  }

  @GetMapping("/recommend")
  @Operation(
      summary = "하루 음식 추천",
      description = "LLM으로부터 아침 점심 저녁 메뉴를 추천받습니다."
  )
  public Api<List<FoodRecommendation>> recommendFood(@LoginUser UserDTO userDTO) {
    return Api.OK(foodBusiness.recommendFood(userDTO));

  }

  @GetMapping
  @Operation(
      summary = "음식 키워드 검색",
      description = "키워드로 음식을 검색하여 조회합니다."
  )
  public Api<List<FoodResponse>> getByKeyword(@LoginUser UserDTO userDTO, @RequestParam String keyword) {
    return Api.OK(foodBusiness.getByKeyword(userDTO, keyword));
  }

  @GetMapping("/today")
  @Operation(
      summary = "일일 식단 조회",
      description = "오늘 먹은 식단을 조회합니다."
  )
  public Api<List<FoodResponse>> getByToday(@LoginUser UserDTO userDTO) {
    return Api.OK(foodBusiness.getByToday(userDTO));
  }

  @GetMapping("/weekly")
  @Operation(
      summary = "주간 식단 조회",
      description = "일주일 동안 먹은 식단을 조회합니다."
  )
  public Api<List<FoodResponse>> getByWeekly(@LoginUser UserDTO userDTO) {
    return Api.OK(foodBusiness.getByWeekly(userDTO));
  }
}
