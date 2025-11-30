package com.mirae.DailyBoost.exercise.domain.service;

import com.mirae.DailyBoost.exercise.domain.repository.Exercise;
import com.mirae.DailyBoost.exercise.domain.repository.ExerciseRepository;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mirae.DailyBoost.food.domain.repository.enums.FoodStatus;
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

  public List<Exercise> getByStatusAndToday(Long userId, ExerciseStatus exerciseStatus, LocalDate now) {
      return exerciseRepository.findByUser_idAndStatusAndRegisteredAt(userId, exerciseStatus, now);
  }
}
