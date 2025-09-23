package com.mirae.DailyBoost.calendars.domain.event.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventDeleteRequest {

  private Long id;
  private Long calendarId;

}
