package com.mirae.DailyBoost.image.domain.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileUtils {

  public static String fileNameConvert(UUID uuid, String fileName) {
    StringBuilder builder = new StringBuilder();
    String extension = getExtension(fileName); // 확장자 추출

    builder.append(uuid).append(".").append(extension); // FileName

    return builder.toString();
  }

  public static String serverNameConvert(UUID uuid, String fileName, LocalDate registeredAt) {
    StringBuilder builder = new StringBuilder();

    String formatedDate = registeredAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    builder.append(formatedDate).append("/").append(uuid).append(".").append(getExtension(fileName));
    return builder.toString();

  }

  // 확장자 추출
  public static String getExtension(String fileName) {
    int pos = fileName.lastIndexOf(".");

    return fileName.substring(pos + 1);
  }
}
