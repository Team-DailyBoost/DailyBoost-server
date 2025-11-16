package com.mirae.DailyBoost.image.domain.image.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.errorCode.ImageErrorCode;
import com.mirae.DailyBoost.image.domain.exception.image.FileIsNullException;
import com.mirae.DailyBoost.image.domain.exception.image.FileSizeExceededException;
import com.mirae.DailyBoost.image.domain.exception.image.InvalidFileTypeException;
import com.mirae.DailyBoost.image.domain.image.business.model.FileUploadDto;
import com.mirae.DailyBoost.image.domain.image.converter.ImageConverter;
import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.image.domain.image.service.ImageService;
import com.mirae.DailyBoost.image.domain.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Business
@Slf4j
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ImageBusiness {

  @Value("${file.dir}")
  private String filePath;
  private String baseUrl = "https://dailyBoost.duckdns.org/uploads/"; // 어떻게 하면 좋을까..?

  private final ImageService imageService;
  private final ImageConverter imageConverter;
  private final MessageConverter messageConverter;

  public Image save(MultipartFile file) {

    FileUploadDto fileUploadDto = saveFile(file);

    Image image = imageConverter.toEntity(fileUploadDto);
    imageService.save(image);

    return image;

  }

  private FileUploadDto saveFile(MultipartFile file) {
    UUID uuid = UUID.randomUUID();
    List<String> allowed = List.of("jpg", "jpeg", "png",  "webp");

    if (file == null) {
      throw new FileIsNullException(ImageErrorCode.FILE_IS_NULL);
    }

      String saveName = FileUtils.fileNameConvert(uuid, file.getOriginalFilename());
      String serverName = FileUtils.serverNameConvert(uuid, file.getOriginalFilename(),
          LocalDate.now());
      String extension = FileUtils.getExtension(file.getOriginalFilename());

      if (!allowed.contains(extension)) {
        throw new InvalidFileTypeException(ImageErrorCode.INVALID_FILE_TYPE);
      }

    long size = file.getSize();
    if (size > 6 * 1024 * 1024) { // 6MB 넘으면
        throw new FileSizeExceededException(ImageErrorCode.FILE_SIZE_EXCEEDED);
      }

    FileUploadDto fileUploadDto = null;

    try {
      log.info("file path : {}", filePath);
      File localFile = new File(filePath + "/" + saveName); // /Users/mirae/upload-dir/ {saveName}
      file.transferTo(localFile);
      fileUploadDto = FileUploadDto.builder()
          .originalName(file.getOriginalFilename())
          .saveName(saveName)
          .serverName(serverName)
          .size(file.getSize())
          .extension(extension)
          .url(baseUrl + saveName)
          .build();
      log.info("파일 저장 완료: {}", localFile.getCanonicalPath());
    } catch (IllegalStateException | IOException e) {
      throw new RuntimeException(e);
    }
    return fileUploadDto;
  }

}
