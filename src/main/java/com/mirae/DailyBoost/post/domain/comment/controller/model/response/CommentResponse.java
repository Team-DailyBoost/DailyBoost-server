package com.mirae.DailyBoost.post.domain.comment.controller.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

  private String comment;
  private LocalDateTime createAt;
  private Long likeCount;
  private Long unLikeCount;

}
