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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {

  private final CommentBusiness commentBusiness;

  @PostMapping("/create")
  public Api<MessageResponse> create(@LoginUser UserDTO userDTO, @RequestBody @Valid CommentRequest commentRequest) {
    return Api.OK(commentBusiness.create(userDTO, commentRequest));
  }

  @PostMapping("/unregister")
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO, @RequestBody @Valid CommentUnregisterRequest commentUnregisterRequest) {
    return Api.OK(commentBusiness.unregister(userDTO, commentUnregisterRequest));
  }

  @PostMapping("/update")
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO, @RequestBody @Valid CommentUpdateRequest commentUpdateRequest) {
    return Api.OK(commentBusiness.update(userDTO, commentUpdateRequest));
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
