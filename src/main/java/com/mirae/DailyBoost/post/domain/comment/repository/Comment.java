package com.mirae.DailyBoost.post.domain.comment.repository;

import com.mirae.DailyBoost.post.domain.comment.repository.enums.CommentStatus;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.user.domain.repository.User;
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
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

  @Column(name = "comment_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  private CommentStatus status;

  @Column(name = "create_at")
  private LocalDateTime createAt;

  @Column(name = "update_at",
      nullable = true)
  private LocalDateTime updatedAt;

  @Column(name = "unregistered_at",
      nullable = true)
  private LocalDateTime unregisterAt;

  @PositiveOrZero
  private Long likeCount = 0L;

  @PositiveOrZero
  private Long unLikeCount = 0L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public void unregister() {
    this.status = CommentStatus.UNREGISTERED;
    this.unregisterAt = LocalDateTime.now();
  }

  public void updateInfo(String content) {
    this.content = content;
    this.updatedAt = LocalDateTime.now();
  }

  public void like() {
    this.likeCount++;
  }

  public void unLike() {
    this.unLikeCount++;
  }
}
