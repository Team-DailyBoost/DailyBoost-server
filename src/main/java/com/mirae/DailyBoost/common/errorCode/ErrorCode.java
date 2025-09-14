package com.mirae.DailyBoost.common.errorCode;

public interface ErrorCode {

  public Integer getHttpCode();
  public Integer getErrorCode();
  public String getDescription();
}
