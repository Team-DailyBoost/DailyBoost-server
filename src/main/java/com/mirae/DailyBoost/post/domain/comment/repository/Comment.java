package com.mirae.DailyBoost.post.domain.comment.repository;

import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.post.domain.comment.repository.enums.CommentStatus;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  @OneToOne(mappedBy = "comment", cascade = CascadeType.ALL)
  private Image image;

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

    public void initImage(Image image) {
        this.image = image;
    }
}
