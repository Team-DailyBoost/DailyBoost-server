package com.mirae.DailyBoost.post.domain.post.controller.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostResponse {
  private Long id;
  private String title;
  private String content;
  private String authorName;
  private Long viewCount;
  private Long likeCount;
  private Long unLikeCount;
  private Long commentCount;

}
