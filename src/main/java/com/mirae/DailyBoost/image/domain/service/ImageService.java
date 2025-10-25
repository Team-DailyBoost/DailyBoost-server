package com.mirae.DailyBoost.image.domain.service;

import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.image.domain.image.repository.ImageRepository;
import com.mirae.DailyBoost.image.domain.image.repository.enums.ImageStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;

  public Image save(Image image) {
    return imageRepository.save(image);
  }

  public Optional<Image> getByIdAndStatus(Long id, ImageStatus status) {
    return imageRepository.findByIdAndStatus(id, status);
  }

  public Optional<Image> getByIdAndPostIdAndStatus(Long id, Long postId, ImageStatus status) {
    return imageRepository.findByIdAndPost_IdAndStatus(id, postId, status);
  }

}
