package com.mirae.DailyBoost.user.exception;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

  @ExceptionHandler(value = UserNotFoundException.class)
  public ResponseEntity<Api<Object>> userNotFoundException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(UserErrorCode.USER_NOT_FOUND));
  }

}
