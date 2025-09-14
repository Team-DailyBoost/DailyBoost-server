package com.mirae.DailyBoost.exercise.domain.converter;

import com.mirae.DailyBoost.common.annotation.Converter;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseInfoDto;
import com.mirae.DailyBoost.exercise.domain.controller.model.request.ExerciseRecommendation;
import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import java.util.List;

@Converter
public class ExerciseConverter {

  public List<Exercise> toEntity(ExerciseRecommendation exerciseRecommendation) {
    return exerciseRecommendation.getExerciseInfoDto().stream()
        .map(exerciseInfoDto -> {

          Exercise exercise = Exercise.builder()
              .name(exerciseInfoDto.getName())
              .description(exerciseInfoDto.getDescription())
              .youtubeLink(exerciseInfoDto.getYoutubeLinks())
              .level(exerciseInfoDto.getLevel())
              .build();
          return exercise;

        }).toList();

  }

}
