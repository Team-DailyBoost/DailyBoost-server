package com.mirae.DailyBoost.openChatAI.controller.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FoodRecommendation {

  List<FoodInfoDto> foodInfoDto;

}
