package com.mirae.DailyBoost.calendars.domain.event.controller.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventResponse {

  private Long id;
  private Long calendarId;
  private String title;
  private String description;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

}
