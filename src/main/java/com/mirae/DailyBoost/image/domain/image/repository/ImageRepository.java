package com.mirae.DailyBoost.image.domain.image.repository;

import com.mirae.DailyBoost.image.domain.image.repository.enums.ImageStatus;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByPostAndStatus(Post post, ImageStatus status);

  Optional<Image> findByIdAndStatus(Long id, ImageStatus status);

  Optional<Image> findByIdAndPost_IdAndStatus(Long id, Long postId, ImageStatus status);
}
