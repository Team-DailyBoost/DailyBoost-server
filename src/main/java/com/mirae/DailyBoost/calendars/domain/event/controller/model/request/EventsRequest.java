package com.mirae.DailyBoost.calendars.domain.event.controller.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class EventsRequest {
  @NotNull
  private Long calendarId;

  @NotNull
  private String title;

  @NotNull
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
  private LocalDateTime startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
  private LocalDateTime endTime;


}
