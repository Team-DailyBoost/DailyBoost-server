package com.mirae.DailyBoost.user.exception.user;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class VerificationCodeExpiredOrNotFoundException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String description;


  public VerificationCodeExpiredOrNotFoundException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }
}
