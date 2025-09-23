package com.mirae.DailyBoost.calendars.domain.event.controller;

import com.mirae.DailyBoost.calendars.domain.event.business.EventBusiness;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventDeleteRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventUpdateRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.request.EventsRequest;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventResponse;
import com.mirae.DailyBoost.calendars.domain.event.controller.model.response.EventsResponse;
import com.mirae.DailyBoost.common.annotation.LoginUser;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventApiController {

  private final EventBusiness eventBusiness;

  @PostMapping("/create")
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO, @RequestBody @Valid EventsRequest eventsRequest) {
    return Api.OK(eventBusiness.create(userDTO, eventsRequest));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO, @RequestBody EventUpdateRequest eventUpdateRequest) {
    return Api.OK(eventBusiness.update(userDTO, eventUpdateRequest));
  }

  @PostMapping("/delete")
  public Api<MessageResponse> delete(@LoginUser UserDTO userDTO, @RequestBody EventDeleteRequest eventDeleteRequest) {
    return Api.OK(eventBusiness.delete(userDTO, eventDeleteRequest));
  }

  @GetMapping("{calendarId}")
  public Api<EventsResponse> getListByCalendarAndRange(@LoginUser UserDTO userDTO,
      @PathVariable Long calendarId,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime rangeStart,
      @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime rangeEnd
      ) {
    return Api.OK(eventBusiness.getListByCalendarAndRange(userDTO, calendarId, rangeStart, rangeEnd));

  }

  @GetMapping
  public Api<EventResponse> getByIdAndCalendarId(
      @RequestParam Long eventId,
      @RequestParam Long calendarId
  ) {
    return Api.OK(eventBusiness.getByIdAndCalendarId(eventId, calendarId));
  }

}
