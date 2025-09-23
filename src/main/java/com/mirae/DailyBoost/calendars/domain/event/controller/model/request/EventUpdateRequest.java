package com.mirae.DailyBoost.calendars.domain.event.controller.model.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventUpdateRequest {
  private Long id;
  private Long calendarId;
  private String title;
  private String description;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

}
