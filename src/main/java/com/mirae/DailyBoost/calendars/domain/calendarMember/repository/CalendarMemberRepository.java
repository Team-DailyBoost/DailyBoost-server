package com.mirae.DailyBoost.calendars.domain.calendarMember.repository;

import com.mirae.DailyBoost.calendars.domain.calendars.repository.enums.CalendarRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarMemberRepository extends JpaRepository<CalendarMember, Long> {

  @Query("""
        select cm from CalendarMember cm
        where cm.user.id = :userId
        """)
  List<CalendarMember> findAllByUserId(@Param("userId") Long userId);

  @Query("""
        select cm from CalendarMember cm
        where cm.calendar.id = :calendarId
        """)
  List<CalendarMember> findAllByCalendarId(@Param("calendarId") Long calendarId);

  @Query("""
        select cm from CalendarMember cm
        where cm.calendar.id = :calendarId
          and cm.user.id = :userId
          and cm.role <> :calendarRole
        """)
  Optional<CalendarMember> findByIfNotRole(@Param("calendarId") Long calendarId,
      @Param("userId") Long userId, @Param("calendarRole") CalendarRole calendarRole);

  // calendar에 속한 userId가 있는지 확인
  Boolean existsByCalendar_IdAndUser_Id(Long calendarId, Long userId);

  Boolean existsByCalendar_IdAndUser_IdAndRole(Long calendarId, Long userId, CalendarRole role);

  Boolean existsByCalendar_IdAndUser_IdAndRoleIn(Long calendar_id, Long user_id,
      List<CalendarRole> roles);

}
