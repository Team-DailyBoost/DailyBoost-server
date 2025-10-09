package com.mirae.DailyBoost.user.exception.user;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class UserNotFoundException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String description;


  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }
}
