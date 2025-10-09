package com.mirae.DailyBoost.exercise.domain.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.exercise.domain.converter.ExerciseConverter;
import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.exercise.domain.service.ExerciseService;
import com.mirae.DailyBoost.global.model.MessageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ExerciseBusiness {

  private final ExerciseService exerciseService;
  private final ExerciseConverter exerciseConverter;
  private final MessageConverter messageConverter;

  public Api<MessageResponse> register(ExerciseRecommendation exerciseRecommendation) {
    List<Exercise> exercises = exerciseConverter.toEntity(exerciseRecommendation);

    exercises.stream().forEach(exercise ->
        exerciseService.register(exercise));

    MessageResponse response = messageConverter.toResponse("운동이 기록되었습니다.");
    return Api.OK(response);

  }
}
