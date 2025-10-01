package com.mirae.DailyBoost.post.domain.post.controller.model.request;

import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import jakarta.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {

  @NotBlank
  private Long id;

  @NotBlank
  @Size(max = 100, message = "제목은 최대 100자까지 가능합니다.")
  private String title;

  @NotBlank
  @Size(max = 1000, message = "제목은 최대 1000자까지 가능합니다.")
  private String content;

  private PostKind postKind;

}
