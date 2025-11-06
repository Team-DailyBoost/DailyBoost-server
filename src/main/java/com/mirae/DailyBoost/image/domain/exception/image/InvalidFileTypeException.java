package com.mirae.DailyBoost.image.domain.exception.image;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class InvalidFileTypeException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String description;


  public InvalidFileTypeException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();

  }
}
