package com.mirae.DailyBoost.calendars.domain.event.repository;

import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {

  @Id @Column(name = "event_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "calendar_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_events_calendar"))
  private Calendars calendar;

  @Column(nullable = false, length = 200)
  private String title;

  private String description;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist // 데이터베이스에 처음으로 저장되기 직전에 메서드를 자동으로 호출
  @PreUpdate // 데이터가 변경 되기 직전에 지정된 메서드 자동 호출
  private void validateTime() {
    if (startTime.isAfter(endTime)) {
      throw new IllegalArgumentException("startTime must be <= endTime");
    }
  }

  public void updateInfo(String title, String description,
      LocalDateTime startTime, LocalDateTime endTime) {
    this.title = title;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;

  }



}
