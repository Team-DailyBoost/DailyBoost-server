package com.mirae.DailyBoost.calendars.domain.calendars.service;

import com.mirae.DailyBoost.calendars.domain.calendars.repository.CalendarRepository;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final CalendarRepository calendarRepository;

  public Calendars save(Calendars calendar) {
    return calendarRepository.save(calendar);
  }

  public List<Calendars> getListByUserId(Long userId) {
    return calendarRepository.findByUser_Id(userId);
  }

  public Optional<Calendars> getById(Long id) {
    return calendarRepository.findById(id);
  }

  public void delete(Calendars calendar) {
    calendarRepository.delete(calendar);
  }

  public Boolean validateDuplicateName(Long userId, String name) {
    return calendarRepository.existsByUser_IdAndName(userId, name);
  }





}
