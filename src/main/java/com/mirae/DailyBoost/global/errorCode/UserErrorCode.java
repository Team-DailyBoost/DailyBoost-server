package com.mirae.DailyBoost.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

  USER_NOT_FOUND(404, 1100, "사용자를 찾을 수 없습니다."),
  ALREADY_UNREGISTERED(HttpStatus.CONFLICT.value(), 1101, "계정이 이미 삭제된 상태입니다. 로그인을 하려면 계정 복구를 해주세요."),
  DORMANT_ACCOUNT(HttpStatus.LOCKED.value(), 1102, "회원님의 계정이 휴면계정입니다. 로그인을 하려면 계정 복구를 해주세요."),
  VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST.value(), 1103, "인증 코드가 일치하지 않습니다."),
  VERIFICATION_CODE_EXPIRED_OR_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 1104, "인증 코드가 만료되었거나 존재하지 않습니다."),
  USER_UNREGISTER_FAIL(500, 1105, "회원 탈퇴에 실패했습니다.")
  ;

  private final Integer httpCode;
  private final Integer errorCode;
  private final String description;
}
