package com.mirae.DailyBoost.post.domain.post.controller.model.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private String title;
  private String content;
  private String authorName;
  private LocalDateTime createdAt;
  private Long viewCount;
  private Long commentCount;
  private Long likeCount;
  private Long unLikeCount;
  private List<CommentInfo> commentInfos;

}
