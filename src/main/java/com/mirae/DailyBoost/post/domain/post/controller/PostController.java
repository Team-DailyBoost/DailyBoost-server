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
import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

  private final PostBusiness postBusiness;

  @PostMapping(value = "/create",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO,
                                     @RequestPart @Valid PostCreateRequest postCreateRequest,
                                     @RequestPart(required = false) List<MultipartFile> files) {
    return Api.OK(postBusiness.create(userDTO, postCreateRequest, files));
  }

  @PostMapping("/{postId}")
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @PathVariable Long postId) {
    return Api.OK(postBusiness.unregister(userDTO, postId));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO,
                                     @RequestPart PostUpdateRequest postUpdateRequest,
                                     @RequestPart(required = false) List<MultipartFile> files) {
    return Api.OK(postBusiness.update(userDTO, postUpdateRequest, files));
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
