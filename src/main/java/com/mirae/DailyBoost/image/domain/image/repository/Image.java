package com.mirae.DailyBoost.image.domain.image.repository;

import com.mirae.DailyBoost.image.domain.image.repository.enums.ImageStatus;
import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
  @Column(name = "image_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String url;

  @NotNull
  @Column(name = "server_name")
  private String serverName;

  @NotNull
  @Column(name = "original_name")
  private String originalName;

  @NotNull
  @Column(name ="save_name")
  private String saveName;

  @NotNull
  @Column(length = 10, nullable = false)
  private String extension;

  @Enumerated(EnumType.STRING)
  private ImageStatus status;

  @NotNull
  @Column(name = "registered_at")
  private LocalDate registeredAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private Comment comment;

}
