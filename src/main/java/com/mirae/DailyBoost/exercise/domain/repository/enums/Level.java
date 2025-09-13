package com.mirae.DailyBoost.exercise.domain.repository.enums;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
public enum Level {
  BEGINNER("초급자"),
  INTERMEDIATE("중급자"),
  ADVANCED("상급자");

  final String description;
}
