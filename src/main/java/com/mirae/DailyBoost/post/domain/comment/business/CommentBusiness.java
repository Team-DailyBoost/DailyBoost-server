package com.mirae.DailyBoost.post.domain.comment.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.image.domain.image.business.ImageBusiness;
import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentUnregisterRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentUpdateRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.response.CommentResponse;
import com.mirae.DailyBoost.post.domain.comment.converter.CommentConverter;
import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.comment.service.CommentService;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import com.mirae.DailyBoost.post.domain.post.service.PostService;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.util.List;

import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Business
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class CommentBusiness {

  private final CommentService commentService;
  private final UserService userService;
  private final PostService postService;
  private final ImageBusiness imageBusiness;
  private final CommentConverter commentConverter;
  private final MessageConverter messageConverter;


  public MessageResponse create(UserDTO userDTO, CommentRequest commentRequest, MultipartFile file) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    Post post = postService.getByIdAndStatus(commentRequest.getPostId(), PostStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    Comment comment = commentConverter.toEntity(user, post, commentRequest);

    post.addComment(comment);

      if (!(file == null)) {
          Image uploadImage = imageBusiness.save(file);

          uploadImage.addCommentImage(comment);
          comment.initImage(uploadImage);
      }

    commentService.save(comment);

    return messageConverter.toResponse("댓글이 작성되었습니다.");
  }

  public MessageResponse unregister(UserDTO userDTO, CommentUnregisterRequest commentUnregisterRequest) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Comment comment = commentService.getById(commentUnregisterRequest.getCommentId())
        .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

    validateCommentOwner(comment, user);

    Post post = postService.getById(commentUnregisterRequest.getPostId())
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    Boolean isExistsComment = postService.existsByComments(post.getId(), comment);

    if (!isExistsComment) {
      throw new IllegalArgumentException("게시글에 작성된 댓글이 아닙니다.");
    }

    post.removeComment(comment);

    comment.unregister();

    commentService.save(comment);

    return messageConverter.toResponse("댓글이 삭제되었습니다.");
  }

  public MessageResponse update(UserDTO userDTO, CommentUpdateRequest commentUpdateRequest, MultipartFile file) {
    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Comment comment = commentService.getById(commentUpdateRequest.getCommentId())
        .orElseThrow(() -> new IllegalArgumentException("COMMENT_NOT_FOUND"));

    validateCommentOwner(comment, user);

    Post post = postService.getById(commentUpdateRequest.getPostId())
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    Boolean isExistsComment = postService.existsByComments(post.getId(), comment);

    if (!isExistsComment) {
      throw new IllegalArgumentException("게시글에 작성된 댓글이 아닙니다.");
    }

    comment.updateInfo(commentUpdateRequest.getContent());

      if (!(file == null)) {
          Image uploadImage = imageBusiness.save(file);

          uploadImage.addCommentImage(comment);
          comment.initImage(uploadImage);
      }

      commentService.save(comment);

    return messageConverter.toResponse("댓글이 수정되었습니다.");

  }

  public MessageResponse like(UserDTO userDTO, Long id) {

    Comment comment = commentService.getById(id)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    comment.like();
    commentService.save(comment);

    return messageConverter.toResponse("좋아요가 눌렸습니다."); // 띄우기 X
  }

  public MessageResponse unLike(UserDTO userDTO, Long id) {

    Comment comment = commentService.getById(id)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    comment.unLike();
    commentService.save(comment);

    return messageConverter.toResponse("싫어요가 눌렸습니다."); // 띄우기 X
  }


  @Transactional(readOnly = true)
  public List<CommentResponse> getByPostId(Long postId) {
    Post post = postService.getById(postId)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    List<Comment> comments = post.getComments();

    return commentConverter.toCommentResponse(comments);
  }

  private void validateCommentOwner(Comment comment, User user) {
    Long commentAuthorId = comment.getUser().getId();
    Long userId = user.getId();

    if (commentAuthorId != userId) {
      throw new IllegalArgumentException("편집 권한이 없습니다.");
    }
  }
}
