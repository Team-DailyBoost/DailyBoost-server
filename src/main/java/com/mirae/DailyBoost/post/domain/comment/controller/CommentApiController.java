package com.mirae.DailyBoost.post.domain.comment.controller;

import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.post.domain.comment.business.CommentBusiness;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentUnregisterRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentUpdateRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.response.CommentResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {

  private final CommentBusiness commentBusiness;

  @PostMapping("/create")
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO,
                                     @RequestPart @Valid CommentRequest commentRequest,
                                     @RequestPart(required = false) MultipartFile file) {
    return Api.OK(commentBusiness.create(userDTO, commentRequest, file));
  }

  @PostMapping("/unregister")
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @RequestBody @Valid CommentUnregisterRequest commentUnregisterRequest) {
    return Api.OK(commentBusiness.unregister(userDTO, commentUnregisterRequest));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO,
                                     @RequestPart @Valid CommentUpdateRequest commentUpdateRequest,
                                     @RequestPart (required = false) MultipartFile file) {
    return Api.OK(commentBusiness.update(userDTO, commentUpdateRequest, file));
  }

  // 수정 할 것
  @PostMapping("/like/{commentId}")
  public Api<MessageResponse> like(@LoginUser UserDTO userDTO, @PathVariable Long commentId) {
    return Api.OK(commentBusiness.like(userDTO, commentId));
  }

  // 수정 할 것
  @PostMapping("/unLike/{commentId}")
  public Api<MessageResponse> unLike(@LoginUser UserDTO userDTO, @PathVariable Long commentId) {
    return Api.OK(commentBusiness.unLike(userDTO, commentId));
  }


  @GetMapping("/{postId}")
  public Api<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
    return Api.OK(commentBusiness.getByPostId(postId));
  }

}
