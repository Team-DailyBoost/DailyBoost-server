package com.mirae.DailyBoost.exercise.domain.controller.model.request;

import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseInfoDto {

  String name;
  String description;
  String youtubeLinks;
  Level level;
}
