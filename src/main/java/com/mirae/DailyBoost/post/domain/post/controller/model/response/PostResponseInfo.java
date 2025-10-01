package com.mirae.DailyBoost.post.domain.post.controller.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseInfo {

  private Long id;
  private String title;
  private String content;
  private String authorName;
  private Long viewCount;
  private Long likeCount;
  private Long unLikeCount;
  private Long commentCount;
}
