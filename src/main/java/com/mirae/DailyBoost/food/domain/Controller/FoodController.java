package com.mirae.DailyBoost.food.domain.Controller;

import com.mirae.DailyBoost.common.annotation.LoginUser;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.food.domain.Controller.model.request.FoodKindRequest;
import com.mirae.DailyBoost.food.domain.Controller.model.request.FoodNameRequest;
import com.mirae.DailyBoost.food.domain.Controller.model.response.FoodResponse;
import com.mirae.DailyBoost.food.domain.business.FoodBusiness;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.openChatAI.controller.model.request.FoodRecommendation;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

  private final FoodBusiness foodBusiness;

  @PostMapping("/register")
  public Api<MessageResponse> register(@LoginUser UserDTO userDTO,
      @RequestBody FoodRecommendation foodRecommendation) {
    return Api.OK(foodBusiness.register(userDTO, foodRecommendation));

  }

  @GetMapping("/kind/{foodId}")
  public Api<FoodResponse> getFoodByKind(@PathVariable Long foodId, @RequestBody FoodKindRequest foodKindRequest) {
    return Api.OK(foodBusiness.getByKind(foodId, foodKindRequest.getFoodKind()));

  }

  @GetMapping("/name/{foodId}")
  public Api<FoodResponse> getFoodByName(@PathVariable Long foodId, @RequestBody FoodNameRequest foodNameRequest) {
    return Api.OK(foodBusiness.getByName(foodId, foodNameRequest.getName()));
  }
}
