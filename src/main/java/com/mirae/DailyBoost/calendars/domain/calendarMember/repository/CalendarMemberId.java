package com.mirae.DailyBoost.calendars.domain.calendarMember.repository;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CalendarMemberId  implements Serializable {
  private Long calendarId;
  private Long userId;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalendarMemberId that = (CalendarMemberId) o;
    return Objects.equals(calendarId, that.calendarId) && Objects.equals(userId,
        that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calendarId, userId);
  }
}

