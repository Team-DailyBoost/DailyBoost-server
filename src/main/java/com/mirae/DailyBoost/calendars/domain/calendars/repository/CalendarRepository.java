package com.mirae.DailyBoost.calendars.domain.calendars.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendars, Long> {
  @EntityGraph(attributePaths = {"user"})
  List<Calendars> findByUser_Id(Long userId);

  Boolean existsByUser_IdAndName(Long userId, String name);
}
