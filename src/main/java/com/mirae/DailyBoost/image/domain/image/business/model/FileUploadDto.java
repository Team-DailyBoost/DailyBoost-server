package com.mirae.DailyBoost.image.domain.image.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadDto {
  private String originalName;
  private String saveName;
  private String serverName;
  private long size;
  private String extension;
  private String url;

}
