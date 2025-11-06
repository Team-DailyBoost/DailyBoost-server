package com.mirae.DailyBoost.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCode {

  FILE_IS_NULL(HttpStatus.BAD_REQUEST.value(), 1200, "업로드할 파일이 비어있습니다."),
  INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST.value(), 1201, "허용되지 않은 파일 형식입니다."),
  FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE.value(), 1202, "파일 크기가 제한을 초과했습니다."),
  FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1203, "파일 저장 중 오류가 발생했습니다."),
  IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1204, "이미지를 찾을 수 없습니다."),
  IMAGE_ALREADY_DELETED(HttpStatus.CONFLICT.value(), 1205, "이미지가 이미 삭제된 상태입니다.");


  private final Integer httpCode;
  private final Integer errorCode;
  private final String description;
}
