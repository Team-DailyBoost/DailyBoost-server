package com.mirae.DailyBoost.post.domain.comment.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
  @NotNull
  private Long postId;

  @NotNull
  private Long commentId;

  @NotBlank(message = "댓글은 비어 있을 수 없습니다.")
  @Size(max = 300, message = "최대 300자까지 작성 가능합니다.")
  private String content;


}
