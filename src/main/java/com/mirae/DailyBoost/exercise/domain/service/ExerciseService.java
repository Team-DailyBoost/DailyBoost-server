package com.mirae.DailyBoost.exercise.domain.service;

import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.exercise.domain.repository.ExerciseRepository;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

  private final ExerciseRepository exerciseRepository;

  public void save(Exercise exercise) {
    exerciseRepository.save(exercise);
  }

  public void register(Exercise exercise) {
    exercise.register();
    exerciseRepository.save(exercise);
  }

  public void saveAll(List<Exercise> exercises) {
    exerciseRepository.saveAll(exercises);
  }

  public Optional<Exercise> getExercise(Long exerciseId, ExerciseStatus status) {
    return exerciseRepository.findByIdAndStatus(exerciseId, status);
  }

}
