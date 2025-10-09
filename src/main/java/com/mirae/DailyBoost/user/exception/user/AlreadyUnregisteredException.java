package com.mirae.DailyBoost.user.exception.user;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class AlreadyUnregisteredException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String description;


  public AlreadyUnregisteredException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();

  }
}
