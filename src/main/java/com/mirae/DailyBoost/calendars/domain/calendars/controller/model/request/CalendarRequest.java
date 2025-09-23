package com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarRequest {
  private String name;
  private String color;

}
