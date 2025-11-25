package com.mirae.DailyBoost.exercise.domain.controller.model.request;

import com.mirae.DailyBoost.exercise.domain.repository.enums.ConditionStatus;
import lombok.Getter;

@Getter
public class ExerciseRequest {
  public int exerciseTime;
  public ConditionStatus condition;

}
