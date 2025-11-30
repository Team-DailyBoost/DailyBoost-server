package com.mirae.DailyBoost.exercise.domain.controller.model.response;

import com.mirae.DailyBoost.exercise.domain.repository.enums.ExercisePart;
import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecommendation {
  String name;
  String description;
  String youtubeLink;
  Level level;
  ExercisePart part;
  Integer sets;
  Integer reps;
  Double weight;
}
