package com.mirae.DailyBoost.image.domain.image.controller;

import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.image.domain.image.business.ImageBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

  private final ImageBusiness imageBusiness;

  @Operation(
      summary = "프로필 이미지 업로드",
      description = "사용자 프로필 이미지를 업로드합니다."
  )
  @PostMapping("/upload")
  public Api<MessageResponse> uploadProfileImage(@RequestParam("file") MultipartFile uploadImage) {
    MessageResponse messageResponse = imageBusiness.save(uploadImage);
    return Api.OK(messageResponse);
  }
}
