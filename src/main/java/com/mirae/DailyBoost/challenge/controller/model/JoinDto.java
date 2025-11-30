package com.mirae.DailyBoost.challenge.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JoinDto {

  private Long challengeId;
  private Long userId;
}
