package com.mirae.DailyBoost.exercise.domain.controller.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecommendation {

  private List<ExerciseInfoDto> exerciseInfoDto;

}
