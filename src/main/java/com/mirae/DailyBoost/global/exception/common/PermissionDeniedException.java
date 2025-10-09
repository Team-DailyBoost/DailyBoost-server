package com.mirae.DailyBoost.global.exception.common;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class PermissionDeniedException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String description;

  public PermissionDeniedException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }

}
