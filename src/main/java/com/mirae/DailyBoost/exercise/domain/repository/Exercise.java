package com.mirae.DailyBoost.exercise.domain.repository;


import com.mirae.DailyBoost.exercise.domain.repository.enums.CompletionStatus;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExercisePart;
import com.mirae.DailyBoost.exercise.domain.repository.enums.ExerciseStatus;
import com.mirae.DailyBoost.exercise.domain.repository.enums.Level;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Level level;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ExerciseStatus status;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private CompletionStatus completionStatus;

  @Column(nullable = true, name = "youtube_link")
  private String youtubeLink;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ExercisePart part;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
  private User user;

  public void register() {
    this.status = ExerciseStatus.REGISTERED;
  }

  public void unregister() {
    this.status = ExerciseStatus.UNREGISTERED;
  }

  public void completed() {
    this.completionStatus = CompletionStatus.COMPLETED;
  }

  public void uncompleted() {
    this.completionStatus = CompletionStatus.UNCOMPLETED;
  }





}
