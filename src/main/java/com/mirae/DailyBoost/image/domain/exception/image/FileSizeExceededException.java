package com.mirae.DailyBoost.image.domain.exception.image;

import com.mirae.DailyBoost.global.errorCode.ErrorCode;

public class FileSizeExceededException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String description;


  public FileSizeExceededException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();

  }
}
