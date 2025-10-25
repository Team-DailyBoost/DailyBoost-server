package com.mirae.DailyBoost.image.domain.image.converter;

import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.image.domain.image.business.model.FileUploadDto;
import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.image.domain.image.repository.enums.ImageStatus;
import java.time.LocalDate;

@Converter
public class ImageConverter {


  public Image toEntity(FileUploadDto fileUploadDto) {
    return Image.builder()
        .originalName(fileUploadDto.getOriginalName())
        .serverName(fileUploadDto.getServerName())
        .saveName(fileUploadDto.getSaveName())
        .registeredAt(LocalDate.now())
        .status(ImageStatus.REGISTERED)
        .url(fileUploadDto.getUrl())
        .extension(fileUploadDto.getExtension())
        .build();
  }
}
