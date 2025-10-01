package com.mirae.DailyBoost.post.domain.post.controller.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfo {
  private Long commentId;
  private String content;
  private LocalDateTime createAt;
  private Long likeCount;
  private Long unLikeCount;
  private String authorName;

}
