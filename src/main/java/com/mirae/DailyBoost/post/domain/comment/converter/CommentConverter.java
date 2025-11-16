package com.mirae.DailyBoost.post.domain.comment.converter;

import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.image.domain.image.repository.Image;
import com.mirae.DailyBoost.post.domain.comment.controller.model.request.CommentRequest;
import com.mirae.DailyBoost.post.domain.comment.controller.model.response.CommentResponse;
import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.comment.repository.enums.CommentStatus;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Converter
public class CommentConverter {

  public Comment toEntity(User user, Post post, CommentRequest commentRequest) {
    return Comment.builder()
        .content(commentRequest.getContent())
        .createAt(LocalDateTime.now())
        .likeCount(0L)
        .unLikeCount(0L)
        .user(user)
        .post(post)
        .status(CommentStatus.REGISTERED)
        .build();
  }

  public List<CommentResponse> toCommentResponse(List<Comment> comments) {
    return comments.stream().map(comment -> {
      return CommentResponse.builder()
          .comment(comment.getContent())
          .createAt(comment.getCreateAt())
          .likeCount(comment.getLikeCount())
          .unLikeCount(comment.getUnLikeCount())
          .imageUrl(Optional.ofNullable(comment.getImage())
                  .map(Image::getUrl)
                  .orElse(null))
          .build();
    }).toList();

  }


}
