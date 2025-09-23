package com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CalendarsResponse {

  List<CalendarResponse> calendarResponses;

}
