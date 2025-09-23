package com.mirae.DailyBoost.calendars.domain.event.service;

import com.mirae.DailyBoost.calendars.domain.event.repository.Event;
import com.mirae.DailyBoost.calendars.domain.event.repository.EventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  public Event save(Event event) {
    return eventRepository.save(event);

  }

  public List<Event> getListByCalendarAndRange(Long calendarId,
      LocalDateTime rangeStart, LocalDateTime rangeEnd) {
    return eventRepository.findByCalendarAndRange(calendarId,rangeStart, rangeEnd);
  }

  public Optional<Event> getByIdAndCalendarId(Long id, Long calendarId) {
    return eventRepository.findByIdAndCalendar_Id(id, calendarId);
  }

  public void delete(Event event) {
    eventRepository.delete(event);
  }
}
