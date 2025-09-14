package com.mirae.DailyBoost.exercise.domain.repository;


import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Exercise {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "exercise_id")
  private Long id;

  @Column(nullable = true)
  private String name;

  @Column(nullable = true)
  private String description;

  @Column(nullable = true)
  @Enumerated(value = EnumType.STRING)
  private Level level;

  @Column(nullable = true)
  @Enumerated(value = EnumType.STRING)
  private ExerciseStatus status;

  @Column(nullable = true, name = "youtube_link")
  private String youtubeLink;

//  private User userId;

  public void initStatus() {
    this.status = ExerciseStatus.UNCOMPLETED;
  }





}
