package com.mirae.DailyBoost.exercise.domain.controller.model.response;

import com.mirae.DailyBoost.exercise.domain.repository.enums.ExercisePart;
import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResponse {

    Long id;
    String name;
    String description;
    String youtubeLink;
    Level level;
    ExercisePart part;

}
