package com.mirae.DailyBoost.exercise.domain.controller;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.exercise.domain.business.ExerciseBusiness;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.global.model.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ExerciseController {
  private final ExerciseBusiness exerciseBusiness;

  @PostMapping("/exercise/register")
  public Api<MessageResponse> register(@RequestBody ExerciseRecommendation exerciseRecommendation) {
    return exerciseBusiness.register(exerciseRecommendation);
  }
}
