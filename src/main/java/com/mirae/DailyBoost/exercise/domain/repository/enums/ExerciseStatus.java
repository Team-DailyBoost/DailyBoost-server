package com.mirae.DailyBoost.exercise.domain.repository.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExerciseStatus {

  COMPLETED("완료"),
  UNCOMPLETED("미완료");

  final String description;
}

