package com.mirae.DailyBoost.common.api;

import com.mirae.DailyBoost.common.errorCode.ErrorCode;

public record Api<T>(Integer errorCode, String description, T value) {

  public static <T> Api<T> OK(T result) {
    return new Api<>(200, "OK", result);
  }

  public static <T> Api<T> error(ErrorCode errorCode) {
    return new Api<>(errorCode.getErrorCode(), errorCode.getDescription(), null);
  }

}
