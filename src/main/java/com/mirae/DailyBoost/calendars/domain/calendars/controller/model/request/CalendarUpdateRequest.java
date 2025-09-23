package com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CalendarUpdateRequest {

  private Long calendarId;
  private String name;
  private String color;
}
