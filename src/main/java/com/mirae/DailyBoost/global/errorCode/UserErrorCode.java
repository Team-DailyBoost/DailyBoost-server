package com.mirae.DailyBoost.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

  USER_NOT_FOUND(404, 1100, "사용자를 찾을 수 없습니다."),
  EXISTS_USER_EMAIL(403, 1101, "이미 존재하는 아이디입니다."),
  EXISTS_USER_NAME(403, 1102, "이미 존재하는 이름입니다."),
  LOGIN_FAIL(401, 1103, "로그인 정보가 일치하지 않습니다."),
  USER_UNREGISTER_FAIL(500, 1104, "회원 탈퇴에 실패했습니다.")
  ;

  private final Integer httpCode;
  private final Integer errorCode;
  private final String description;
}
