package com.mirae.DailyBoost.post.domain.comment.controller.model.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUnregisterRequest {

  @NotNull
  private Long postId;
  @NotNull
  private Long commentId;
}
