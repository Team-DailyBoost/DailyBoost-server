package com.mirae.DailyBoost.exercise.domain.repository;

import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

  Optional<Exercise> findByIdAndStatus(Long id, ExerciseStatus status);


}
