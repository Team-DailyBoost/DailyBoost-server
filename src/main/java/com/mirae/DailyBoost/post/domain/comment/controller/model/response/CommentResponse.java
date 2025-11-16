package com.mirae.DailyBoost.post.domain.comment.controller.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

  private String comment;
  private LocalDateTime createAt;
  private Long likeCount;
  private Long unLikeCount;

  @Nullable
  private String imageUrl;

}
