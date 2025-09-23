package com.mirae.DailyBoost.calendars.domain.calendars.business;

import com.mirae.DailyBoost.calendars.domain.calendarMember.repository.CalendarMember;
import com.mirae.DailyBoost.calendars.domain.calendarMember.service.CalendarMemberService;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.AddUsersEmailRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.CalendarUpdateRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.request.CalendarRequest;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarResponse;
import com.mirae.DailyBoost.calendars.domain.calendars.controller.model.response.CalendarsResponse;
import com.mirae.DailyBoost.calendars.domain.calendars.converter.CalendarConverter;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.Calendars;
import com.mirae.DailyBoost.calendars.domain.calendars.repository.enums.CalendarRole;
import com.mirae.DailyBoost.calendars.domain.calendars.service.CalendarService;
import com.mirae.DailyBoost.common.annotation.Business;
import com.mirae.DailyBoost.common.converter.MessageConverter;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class CalendarBusiness {

  private final CalendarService calendarService;
  private final CalendarMemberService calendarMemberService;
  private final UserService userService;
  private final CalendarConverter calendarConverter;
  private final MessageConverter messageConverter;

  public MessageResponse create(UserDTO userDTO, CalendarRequest calendarRequest) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    // 이름 중복 방지
    Boolean validateName = calendarService.validateDuplicateName(userDTO.getId(), calendarRequest.getName());

    if (validateName) {
      throw new IllegalArgumentException("이미 존재하는 캘린더 이름입니다.");
    }

    Calendars calendar = calendarConverter.toEntity(user, calendarRequest);

    calendarService.save(calendar);

    // 캘린더, 사용자를 담고 OWNER 권한 주기
    calendarMemberService.save(CalendarMember.of(calendar, user, CalendarRole.OWNER));
    return messageConverter.toResponse("캘린더가 생성되었습니다.");
  }

  public MessageResponse delete(UserDTO userDTO, Long calendarId) {

    Calendars calendar = calendarService.getById(calendarId)
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    Boolean isOwner = calendarMemberService.existsUserByCalendarIdAndUserIdAndRole(calendarId,
        userDTO.getId(), CalendarRole.OWNER);

    if(!isOwner) {
      throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }

    // 캘린더 멤버 삭제 후 캘린더 삭제
    calendar.getMembers().clear();

    calendarService.delete(calendar);

    return messageConverter.toResponse("캘린더가 삭제되었습니다.");
  }

  public MessageResponse update(UserDTO userDTO, CalendarUpdateRequest calendarUpdateRequest) {

    Calendars calendar = calendarService.getById(calendarUpdateRequest.getCalendarId())
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    Boolean isOwner = calendarMemberService.existsUserByCalendarIdAndUserIdAndRole(calendar.getId(),
        userDTO.getId(), CalendarRole.OWNER);

    if (!isOwner) {
      throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

    calendar.update(calendarUpdateRequest.getName(), calendarUpdateRequest.getColor());

    calendarService.save(calendar);

    return messageConverter.toResponse("캘린더가 수정되었습니다.");
  }

  @Transactional(readOnly = true)
  public CalendarResponse getById(Long calendarId) {

    Calendars calendar = calendarService.getById(calendarId)
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    return calendarConverter.toResponse(calendar);
  }

  @Transactional(readOnly = true)
  public CalendarsResponse myAllCalendars(UserDTO userDTO) {

    List<Calendars> calendars = calendarService.getListByUserId(userDTO.getId());

    return calendarConverter.toListResponse(calendars);
  }

  public MessageResponse addMember(UserDTO userDTO, Long calendarId, AddUsersEmailRequest req) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Boolean isOwner = calendarMemberService.existsUserByCalendarIdAndUserIdAndRole(calendarId,
        userDTO.getId(), CalendarRole.OWNER);

    Calendars calendar = calendarService.getById(calendarId)
        .orElseThrow(() -> new IllegalArgumentException("CALENDAR_NOT_FOUND"));

    if (!isOwner) {
      throw new IllegalArgumentException("캘린더의 OWNER만 초대할 수 있습니다.");
    }

    invite(user, calendar, req);

    calendarService.save(calendar);

    return messageConverter.toResponse("유저가 초대 되었습니다.");
  }

  private void invite(User user, Calendars calendar, AddUsersEmailRequest req) {
    // 초대 유저 목록
    List<User> invitedUsers = userService.getListByEmails(req.getEmails());

    invitedUsers.stream().forEach(invitedUser -> {

          Boolean existsUser = calendarMemberService.existsUserByCalendarIdAndUserId(calendar.getId(),
              invitedUser.getId());

          if (existsUser) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
          }

          if (invitedUser.equals(user)) {
            throw new IllegalArgumentException("자기 자신을 초대할 수 없습니다.");
          }
        });

    // 초대 유저 목록에서 모두 다 초대 (권한 기본값 -> CalendarRole.VIEWER)
    invitedUsers.stream().forEach(invitedUser -> {
      CalendarMember calendarMember = calendarMemberService.save(
          CalendarMember.of(calendar, invitedUser, CalendarRole.VIEWER));

      calendar.addMember(calendarMember);
    });

    /*
    멤버 별 권한 업데이트 구현 예정

    오직 OWNER만 바꿔 줄 수 있음.
    VIEWER -> EDITOR
    EDITOR -> VIEWER
     */

  }


}
