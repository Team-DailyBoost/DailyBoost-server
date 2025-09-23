package com.mirae.DailyBoost.calendars.domain.calendarMember.service;

import com.mirae.DailyBoost.calendars.domain.calendarMember.repository.CalendarMember;
import com.mirae.DailyBoost.calendars.domain.calendarMember.repository.CalendarMemberRepository;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.enums.CalendarRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarMemberService {

  private final CalendarMemberRepository calendarMemberRepository;

  // 사용자가 속한 캘린더 목록
  public List<CalendarMember> getListByUserId(Long userId) {
    return calendarMemberRepository.findAllByUserId(userId);
  }

  // 캘린더에 속한 멤버 목록
  public List<CalendarMember> getListByCalendarId(Long calendarId) {
    return calendarMemberRepository.findAllByCalendarId(calendarId);
  }

  public Optional<CalendarMember> findByIfNotRole(Long calendarId, Long userId, CalendarRole calendarRole) {
    return calendarMemberRepository.findByIfNotRole(calendarId, userId, calendarRole);
  }

  public Boolean existsUserByCalendarIdAndUserId(Long calendarId, Long userId) {
    return calendarMemberRepository.existsByCalendar_IdAndUser_Id(calendarId, userId);
  }

  public Boolean existsUserByCalendarIdAndUserIdAndRole(Long calendarId, Long userId, CalendarRole role) {
    return calendarMemberRepository.existsByCalendar_IdAndUser_IdAndRole(calendarId, userId, role);
  }

  public Boolean existsUserByCalendarIdAndUserIdAndRoleIn(Long calendarId, Long userId, List<CalendarRole> roles) {
    return calendarMemberRepository.existsByCalendar_IdAndUser_IdAndRoleIn(calendarId, userId, roles);
  }

  public CalendarMember save(CalendarMember calendarMember) {
    return calendarMemberRepository.save(calendarMember);
  }


}
