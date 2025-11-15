package com.mirae.DailyBoost.exercise.domain.converter;

import com.mirae.DailyBoost.exercise.domain.repository.enums.CompletionStatus;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.exercise.domain.controller.model.response.ExerciseRecommendation;
import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.user.domain.repository.User;

import java.util.List;

@Converter
public class ExerciseConverter {

  public List<Exercise> toEntity(User user, List<ExerciseRecommendation> exerciseList) {
    return exerciseList.stream()
        .map(exercise -> {

          Exercise exerciseEntity = Exercise.builder()
              .name(exercise.getName())
              .description(exercise.getDescription())
              .youtubeLink(exercise.getYoutubeLink())
              .level(exercise.getLevel())
              .status(ExerciseStatus.UNREGISTERED)
              .completionStatus(CompletionStatus.UNCOMPLETED)
              .part(exercise.getPart()).user(user).build();

          return exerciseEntity;

        }).toList();

  }

}
