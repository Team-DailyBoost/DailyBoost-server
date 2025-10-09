package com.mirae.DailyBoost.calendars.domain.calendars.controller;

import com.mirae.DailyBoost.calendars.domain.calendars.business.CalendarBusiness;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.AddUsersEmailRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.CalendarRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.CalendarUpdateRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarResponse;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarsResponse;
import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarApiController {

  private final CalendarBusiness calendarBusiness;

  @PostMapping("/create")
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO, @RequestBody CalendarRequest calendarRequest) {
    return Api.OK(calendarBusiness.create(userDTO, calendarRequest));
  }

  @PostMapping("/delete/{calendarId}")
  public Api<MessageResponse> delete(@LoginUser UserDTO userDTO, @PathVariable Long calendarId) {
    return Api.OK(calendarBusiness.delete(userDTO, calendarId));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO, @RequestBody CalendarUpdateRequest calendarUpdateRequest) {
    return Api.OK(calendarBusiness.update(userDTO, calendarUpdateRequest));
  }

  @GetMapping("/{calendarId}")
  public Api<CalendarResponse> getCalendar(@PathVariable Long calendarId) {
    return Api.OK(calendarBusiness.getById(calendarId));
  }

  @GetMapping
  public Api<CalendarsResponse> getCalendars(@LoginUser UserDTO userDTO) {
    return Api.OK(calendarBusiness.myAllCalendars(userDTO));
  }

  @PostMapping("/invite/{calendarId}")
  public Api<MessageResponse> invite(@LoginUser UserDTO userDTO, @PathVariable Long calendarId, @RequestBody
      @Valid AddUsersEmailRequest addUsersEmailRequest) {
    return Api.OK(calendarBusiness.addMember(userDTO, calendarId, addUsersEmailRequest));
  }

}
