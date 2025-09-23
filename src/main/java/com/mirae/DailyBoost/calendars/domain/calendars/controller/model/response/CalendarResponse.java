package com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CalendarResponse {
  private Long id;
  private String name;
  private String color;
}
