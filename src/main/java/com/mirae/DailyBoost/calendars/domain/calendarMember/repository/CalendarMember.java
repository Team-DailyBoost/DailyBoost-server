package com.mirae.DailyBoost.calendars.domain.calendarMember.repository;

import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.enums.CalendarRole;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "calendar_members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarMember {

  @EmbeddedId
  CalendarMemberId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("calendarId")
  @JoinColumn(name = "calendar_id",
      foreignKey = @ForeignKey(name = "fk_member_calendar"))
  @OnDelete(action = OnDeleteAction.CASCADE)  // 스키마 생성 시 ON DELETE CASCADE
  private Calendars calendar;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("userId")
  @JoinColumn(name = "user_id",
      foreignKey = @ForeignKey(name = "fk_member_user"))
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CalendarRole role; // OWNER / EDITOR / VIEWER

  @CreationTimestamp
  @Column(nullable = false, name = "add_at")
  private LocalDateTime addedAt;

  public static CalendarMember of(Calendars calendar, User user, CalendarRole role) {
    return CalendarMember.builder()
        .id(new CalendarMemberId(calendar.getId(), user.getId()))
        .calendar(calendar)
        .user(user)
        .role(role)
        .build();
  }
}

