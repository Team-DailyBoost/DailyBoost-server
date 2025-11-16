package com.mirae.DailyBoost.post.domain.post.repository;

import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import com.mirae.DailyBoost.user.domain.repository.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Post {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private PostStatus status;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user; // author

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  @Column(name = "unregistered_at")
  private LocalDateTime unregisteredAt;

  @PositiveOrZero
  private Long viewCount = 0L;

  @PositiveOrZero
  private Long commentCount = 0L;

  @PositiveOrZero
  private Long likeCount = 0L;

  @PositiveOrZero
  private Long unLikeCount = 0L;

  @Enumerated(EnumType.STRING)
  private PostKind postKind;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images = new ArrayList<>();

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
  }

  public void unregister() {
    this.status = PostStatus.UNREGISTERED;
    this.unregisteredAt = LocalDateTime.now();
  }

  public void updateInfo(String title, String content, PostKind postKind) {
    this.title = title;
    this.content = content;
    this.postKind = postKind;
    this.updatedAt = LocalDateTime.now();
  }

  public void addViewCount() {
    this.viewCount++;
  }

  public void like() {
    this.likeCount++;
  }

  public void unLike() {
    this.unLikeCount--;
  }

  public void addImage(Image image) {
    this.images.add(image);
  }
}
