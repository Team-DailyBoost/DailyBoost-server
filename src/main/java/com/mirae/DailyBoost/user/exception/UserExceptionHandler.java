package com.mirae.DailyBoost.user.exception;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.user.exception.user.AlreadyUnregisteredException;
import com.mirae.DailyBoost.user.exception.user.DormantAccountException;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import com.mirae.DailyBoost.user.exception.user.VerificationCodeExpiredOrNotFoundException;
import com.mirae.DailyBoost.user.exception.user.VerificationCodeMismatchException;
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
    log.error("", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(UserErrorCode.USER_NOT_FOUND));
  }

  @ExceptionHandler(value = AlreadyUnregisteredException.class)
  public ResponseEntity<Api<Object>> alreadyUnregisteredException(AlreadyUnregisteredException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Api.error(UserErrorCode.ALREADY_UNREGISTERED));
  }

  @ExceptionHandler(value = DormantAccountException.class)
  public ResponseEntity<Api<Object>> dormantAccountException(DormantAccountException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.LOCKED)
        .body(Api.error(UserErrorCode.DORMANT_ACCOUNT));
  }

  @ExceptionHandler(value = VerificationCodeMismatchException.class)
  public ResponseEntity<Api<Object>> verificationCodeMismatchException(VerificationCodeMismatchException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(UserErrorCode.VERIFICATION_CODE_MISMATCH));
  }

  @ExceptionHandler(value = VerificationCodeExpiredOrNotFoundException.class)
  public ResponseEntity<Api<Object>> verificationCodeExpiredOrNotFoundException(VerificationCodeExpiredOrNotFoundException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(UserErrorCode.VERIFICATION_CODE_EXPIRED_OR_NOT_FOUND));
  }



}
