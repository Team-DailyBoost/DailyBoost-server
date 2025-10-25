package com.mirae.DailyBoost.image.domain.exception;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.errorCode.ImageErrorCode;
import com.mirae.DailyBoost.image.domain.exception.image.FileIsNullException;
import com.mirae.DailyBoost.image.domain.exception.image.FileSizeExceededException;
import com.mirae.DailyBoost.image.domain.exception.image.InvalidFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ImageExceptionHandler {

  @ExceptionHandler(value = FileIsNullException.class)
  public ResponseEntity<Api<Object>> fileIsNullException(FileIsNullException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ImageErrorCode.FILE_IS_NULL));
  }

  @ExceptionHandler(value = InvalidFileTypeException.class)
  public ResponseEntity<Api<Object>> invalidFileTypeException(InvalidFileTypeException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ImageErrorCode.INVALID_FILE_TYPE));
  }

  @ExceptionHandler(value = FileSizeExceededException.class)
  public ResponseEntity<Api<Object>> fileSizeExceededException(FileSizeExceededException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(Api.error(ImageErrorCode.FILE_SIZE_EXCEEDED));
  }

}
