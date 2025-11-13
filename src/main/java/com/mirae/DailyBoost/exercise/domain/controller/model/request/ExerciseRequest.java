package com.mirae.DailyBoost.exercise.domain.controller.model.request;

import com.mirae.DailyBoost.exercise.domain.repository.enums.ExercisePart;
import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseRequest {

  private String userInput;
  private Level level;
  private ExercisePart part;

}
