package com.mirae.DailyBoost.openChatAI.controller;

import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.openChatAI.business.GeminiBusiness;
import com.mirae.DailyBoost.openChatAI.controller.model.request.UserInputRequest;
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
  public Api<ExerciseRecommendation> recommendExercise(@RequestBody UserInputRequest userInputRequest) {
    return geminiBusiness.recommendExercise(userInputRequest);

  }

//  @GetMapping("/recommend/food")
//  public MessageResponse recommendEercise(@RequestBody String userInput) {
//    return geminiBusiness.recommendExercise(userInput);
//
//  }
//
//  @GetMapping("/recommend/resipe")
//  public MessageResponse recommendExercie(@RequestBody String userInput) {
//    return geminiBusiness.recommendExercise(userInput);
//
//  }
}