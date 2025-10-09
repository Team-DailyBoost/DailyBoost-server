package com.mirae.DailyBoost.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

  PERMISSION_DENIED(HttpStatus.FORBIDDEN.value(), 1001, "권한이 없습니다.");

  private final Integer httpCode;
  private final Integer errorCode;
  private final String description;

}
