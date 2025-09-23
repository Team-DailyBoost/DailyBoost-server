package com.mirae.DailyBoost.calendars.domain.event.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  // 기간 겹침 조회 (start, end) 교차
  @Query("""
        select e from Event e
        where e.calendar.id = :calendarId
          and e.startTime < :rangeEnd
          and e.endTime   >= :rangeStart
        order by e.startTime asc
        """)
  List<Event> findByCalendarAndRange(@Param("calendarId") Long calendarId,
      @Param("rangeStart") LocalDateTime rangeStart,
      @Param("rangeEnd") LocalDateTime rangeEnd);

  Optional<Event> findByIdAndCalendar_Id(Long id, Long calendarId);

}

