package com.mirae.DailyBoost.post.domain.post.controller;

import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.post.domain.post.business.PostBusiness;
import com.mirae.DailyBoost.post.domain.post.controller.model.request.PostCreateRequest;
import com.mirae.DailyBoost.post.domain.post.controller.model.request.PostUpdateRequest;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostsResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.SearchPostResponse;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

  private final PostBusiness postBusiness;

  @PostMapping("/create")
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO, @RequestBody @Valid PostCreateRequest postCreateRequest) {
    return Api.OK(postBusiness.create(userDTO, postCreateRequest));
  }

  @PostMapping("/{postId}")
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @PathVariable Long postId) {
    return Api.OK(postBusiness.unregister(userDTO, postId));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO, @RequestBody PostUpdateRequest postUpdateRequest) {
    return Api.OK(postBusiness.update(userDTO, postUpdateRequest));
  }

  // 수정 할 것
  @PostMapping("/like/{postId}")
  public Api<MessageResponse> like(@LoginUser UserDTO userDTO, @PathVariable Long postId) {
    return Api.OK(postBusiness.like(userDTO, postId));
  }

  // 수정 할 것
  @PostMapping("/unLike/{postId}")
  public Api<MessageResponse> unLike(@LoginUser UserDTO userDTO, @PathVariable Long postId) {
    return Api.OK(postBusiness.unLike(userDTO, postId));
  }

  @GetMapping("/posts")
  public Api<List<PostsResponse>> getByPostKind(@RequestParam PostKind postKind) {
    return Api.OK(postBusiness.getByPostKind(postKind));
  }

  @GetMapping("/{postId}")
  public Api<PostResponse> getByPostId(@LoginUser UserDTO userDTO, @PathVariable Long postId) {
    return Api.OK(postBusiness.getByPostId(userDTO, postId));
  }

  @GetMapping
  public Api<List<SearchPostResponse>> getByTitle(@RequestParam String title) {
    return Api.OK(postBusiness.search(title));
  }

}
