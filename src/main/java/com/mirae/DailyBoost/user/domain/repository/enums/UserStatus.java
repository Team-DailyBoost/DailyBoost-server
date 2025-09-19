package com.mirae.DailyBoost.user.domain.repository.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {

  REGISTERED("등록"),
  UNREGISTERED("등록해제"),
  DORMANT("휴먼");

  private String description;

}
