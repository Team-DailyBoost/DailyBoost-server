package com.mirae.DailyBoost.calendars.domain.event.converter;

import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventsRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventInfo;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventResponse;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventsResponse;
import com.mirae.DailyBoost.calendars.domain.event.repository.Event;
import com.mirae.DailyBoost.global.annotation.Converter;
import java.time.LocalDateTime;
import java.util.List;

@Converter
public class EventConverter {

  public Event toEntity(Calendars calendar, EventsRequest eventsRequest) {
    return Event.builder()
        .calendar(calendar)
        .title(eventsRequest.getTitle())
        .description(eventsRequest.getDescription())
        .startTime(eventsRequest.getStartTime())
        .endTime(eventsRequest.getEndTime())
        .createdAt(LocalDateTime.now())
        .build();

  }

  public EventsResponse toResponse(List<Event> events) {

    return EventsResponse.builder()
        .eventInfos(events.stream().map(event -> {
          return EventInfo.builder()
              .id(event.getId())
              .calendarId(event.getCalendar().getId())
              .title(event.getTitle())
              .description(event.getDescription())
              .startTime(event.getStartTime())
              .endTime(event.getEndTime())
              .build();
        }).toList())
        .build();
  }

  public EventResponse toResponse(Event event) {
    return EventResponse.builder()
        .id(event.getId())
        .calendarId(event.getCalendar().getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .startTime(event.getStartTime())
        .endTime(event.getEndTime())
        .build();
  }

}
