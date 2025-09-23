package com.mirae.DailyBoost.calendars.domain.event.business;

import com.mirae.DailyBoost.calendars.domain.calendarMember.repository.CalendarMember;
import com.mirae.DailyBoost.calendars.domain.calendarMember.service.CalendarMemberService;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.enums.CalendarRole;
import com.mirae.DailyBoost.calendars.domain.calendars.service.CalendarService;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventDeleteRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventUpdateRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventsRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventResponse;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventsResponse;
import com.mirae.DailyBoost.calendars.domain.event.converter.EventConverter;
import com.mirae.DailyBoost.calendars.domain.event.repository.Event;
import com.mirae.DailyBoost.calendars.domain.event.service.EventService;
import com.mirae.DailyBoost.common.annotation.Business;
import com.mirae.DailyBoost.common.converter.MessageConverter;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Business
@Slf4j
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class EventBusiness {
  private final EventService eventService;
  private final CalendarMemberService calendarMemberService;
  private final CalendarService calendarService;
  private final EventConverter eventConverter;
  private final MessageConverter messageConverter;

  public MessageResponse create(UserDTO userDTO, EventsRequest eventsRequest) {

    CalendarMember calendarMember = calendarMemberService.findByIfNotRole(
            eventsRequest.getCalendarId(),
            userDTO.getId(), CalendarRole.VIEWER)
        .orElseThrow(() -> new IllegalArgumentException("입력 권한이 없습니다."));

    Calendars calendar = calendarMember.getCalendar();
    if (calendar == null) {
      throw new IllegalArgumentException("CALENDAR_NOT_FOUND");
    }

    Event event = eventConverter.toEntity(calendar, eventsRequest);

    eventService.save(event);

    return messageConverter.toResponse("일정이 생성되었습니다.");

  }

  public MessageResponse update(UserDTO userDTO, EventUpdateRequest req) {

    Event event = eventService.getByIdAndCalendarId(req.getId(),
            req.getCalendarId())
        .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));


    Long eventCalendarId = event.getCalendar().getId();
    if (!eventCalendarId.equals(req.getCalendarId())) {
      throw new IllegalArgumentException("CALENDAR_MISMATCH");
    }

    Calendars calendar = calendarService.getById(req.getCalendarId())
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    Boolean canEdit = calendarMemberService.existsUserByCalendarIdAndUserIdAndRoleIn(req.getCalendarId(),
        userDTO.getId(), List.of(CalendarRole.OWNER, CalendarRole.EDITOR));

    if (!canEdit) {
      throw new IllegalArgumentException("편집 권한이 없습니다.");
    }

    event.updateInfo(req.getTitle(), req.getDescription(), req.getStartTime(),
        req.getEndTime());

    eventService.save(event);

    return messageConverter.toResponse("일정이 수정되었습니다.");
  }

  public MessageResponse delete(UserDTO userDTO, EventDeleteRequest eventDeleteRequest) {

    Calendars calendar = calendarService.getById(eventDeleteRequest.getCalendarId())
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    Boolean isOwner = calendarMemberService.existsUserByCalendarIdAndUserIdAndRole(calendar.getId(),
        userDTO.getId(), CalendarRole.OWNER);

    if (!isOwner) {
      throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }

    Event event = eventService.getByIdAndCalendarId(eventDeleteRequest.getId(), calendar.getId())
        .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

    eventService.delete(event);

    return messageConverter.toResponse("일정이 삭제되었습니다.");
  }

  // 사용자가 범위 안에 있는 일정 목록을 조회
  @Transactional(readOnly = true)
  public EventsResponse getListByCalendarAndRange(UserDTO userDTO, Long calendarId, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
    Calendars calendar = calendarService.getById(calendarId)
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    // 캘린더(calendarId)에 속한 멤버가 존재하는지 검사
    Boolean existsUser = calendarMemberService.existsUserByCalendarIdAndUserId(calendar.getId(),
        userDTO.getId());

    if (!existsUser) {
      throw new IllegalArgumentException("캘린더에 속한 멤버가 아닙니다.");
    }

    List<Event> events = eventService.getListByCalendarAndRange(calendar.getId(),
        rangeStart, rangeEnd);

    return eventConverter.toResponse(events);
  }

  @Transactional(readOnly = true)
  public EventResponse getByIdAndCalendarId(Long eventId, Long calendarId) {
    Event event = eventService.getByIdAndCalendarId(eventId, calendarId)
        .orElseThrow(() -> new IllegalArgumentException("EVENT_NOT_FOUND"));

    return eventConverter.toResponse(event);
  }




}
