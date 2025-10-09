package com.mirae.DailyBoost.post.domain.post.converter;

import com.mirae.DailyBoost.global.annotation.Converter;
import com.mirae.DailyBoost.post.domain.post.controller.model.request.PostCreateRequest;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.CommentInfo;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostsResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.SearchPostResponse;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import com.mirae.DailyBoost.user.domain.repository.User;
import java.time.LocalDateTime;
import java.util.List;

@Converter
public class PostConverter {


  public Post toEntity(User user, PostCreateRequest req) {
    return Post.builder()
        .user(user)
        .title(req.getTitle())
        .content(req.getContent())
        .createdAt(LocalDateTime.now())
        .postKind(req.getPostKind())
        .status(PostStatus.REGISTERED)
        .likeCount(0L)
        .unLikeCount(0L)
        .viewCount(0L)
        .commentCount(0L)
        .build();
  }

  public List<PostsResponse> toPostsResponse(List<Post> posts) {
    return posts.stream().map(post -> {
      return PostsResponse.builder()
          .id(post.getId())
          .title(post.getTitle())
          .content(post.getContent())
          .authorName(post.getUser().getNickname())
          .viewCount(post.getViewCount())
          .likeCount(post.getLikeCount())
          .unLikeCount(post.getUnLikeCount())
          .commentCount(Long.valueOf(post.getComments().size()))
          .build();
          }
    ).toList();
  }


// 익명 기능 삭제
//  public PostResponse toPostResponse(Post post) {
//    if (post.getIsAnonymity()) {
//      return PostResponse.builder()
//          .title(post.getTitle())
//          .content(post.getContent())
//          .authorName("익명의 작성자")
//          .createdAt(post.getCreatedAt())
//          .viewCount(post.getViewCount())
//          .commentCount(post.getCommentCount())
//          .likeCount(post.getLikeCount())
//          .unLikeCount(post.getUnLikeCount())
//          .build();
//    }

    public PostResponse toPostResponse(Post post) {
      return PostResponse.builder()
          .title(post.getTitle())
          .content(post.getContent())
          .authorName(post.getUser().getNickname())
          .createdAt(post.getCreatedAt())
          .viewCount(post.getViewCount())
          .commentCount(Long.valueOf(post.getComments().size()))
          .likeCount(post.getLikeCount())
          .unLikeCount(post.getUnLikeCount())
          .commentInfos(post.getComments().stream().map(comment -> {
            return CommentInfo.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createAt(comment.getCreateAt())
                .likeCount(comment.getLikeCount())
                .unLikeCount(comment.getUnLikeCount())
                .authorName(comment.getUser().getNickname())
                .build();
          }).toList())
          .build();
    }

  public List<SearchPostResponse> toSearchPostResponse(List<Post> searchPosts) {

    return searchPosts.stream().map(searchPost -> {
      return SearchPostResponse.builder()
          .id(searchPost.getId())
          .title(searchPost.getTitle())
          .content(searchPost.getContent())
          .authorName(searchPost.getUser().getNickname())
          .viewCount(searchPost.getViewCount())
          .likeCount(searchPost.getLikeCount())
          .unLikeCount(searchPost.getUnLikeCount())
          .commentCount(Long.valueOf(searchPost.getComments().size()))
          .build();
          }).toList();

  }
}
