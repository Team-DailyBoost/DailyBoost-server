package com.mirae.DailyBoost.exercise.domain.service;

import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.exercise.domain.repository.ExerciseRepository;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

  private final ExerciseRepository exerciseRepository;

  private void register(Exercise exercise) {
    exercise.initStatus();
    exerciseRepository.save(exercise);
  }

  private Optional<Exercise> getExercise(Long exerciseId, ExerciseStatus status) {
    return exerciseRepository.findByIdAndStatus(exerciseId, status);
  }

}
