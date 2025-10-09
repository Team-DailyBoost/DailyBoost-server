package com.mirae.DailyBoost.calendars.domain.calendars.converter;

import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.CalendarRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarResponse;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarsResponse;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.time.LocalDateTime;
import java.util.List;

@Converter
public class CalendarConverter {


  public Calendars toEntity(User user, CalendarRequest req) {
    return Calendars.builder()
        .name(req.getName())
        .color(req.getColor())
        .user(user)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public CalendarResponse toResponse(Calendars calendar) {
    return CalendarResponse.builder()
        .name(calendar.getName())
        .color(calendar.getColor())
        .build();
  }

  public CalendarsResponse toListResponse(List<Calendars> calendars) {
    return CalendarsResponse.builder()
        .calendarResponses(calendars.stream().map(calendar ->
            CalendarResponse.builder()
                .id(calendar.getId())
                .name(calendar.getName())
                .color(calendar.getColor()).build())
            .toList())
        .build();

  }
}
