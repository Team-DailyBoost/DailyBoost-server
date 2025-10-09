package com.mirae.DailyBoost.global.exception;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.errorCode.CommonErrorCode;
import com.mirae.DailyBoost.global.exception.common.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = PermissionDeniedException.class)
  public ResponseEntity<Api<Object>> permissionDeniedException(PermissionDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Api.error(CommonErrorCode.PERMISSION_DENIED));
  }




}
