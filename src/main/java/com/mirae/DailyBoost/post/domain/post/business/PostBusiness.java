package com.mirae.DailyBoost.post.domain.post.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.request.PostCreateRequest;
import com.mirae.DailyBoost.post.domain.post.controller.model.request.PostUpdateRequest;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.PostsResponse;
import com.mirae.DailyBoost.post.domain.post.controller.model.response.SearchPostResponse;
import com.mirae.DailyBoost.post.domain.post.converter.PostConverter;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import com.mirae.DailyBoost.post.domain.post.service.PostService;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PostBusiness {

  private final PostService postService;
  private final UserService userService;
  private final PostConverter postConverter;
  private final MessageConverter messageConverter;

  public MessageResponse create(UserDTO userDTO, PostCreateRequest postCreateRequest) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Boolean validateDuplicationTitle = postService.existsByTitle(user.getId(), postCreateRequest.getTitle(),
        PostStatus.REGISTERED);

    if (validateDuplicationTitle) {
      throw new IllegalArgumentException("내 게시글에 같은 제목의 게시글이 존재합니다.");
    }

    Post post = postConverter.toEntity(user, postCreateRequest);

    postService.save(post);
    return messageConverter.toResponse("게시글이 생성되었습니다.");

  }

  public MessageResponse unregister(UserDTO userDTO, Long id) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Post post = postService.getById(id)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    validatePostAuthor(user, post);

    // 등록해제 까지만 한달이 지난 후, UNREGISTERED -> (DB에서 영구적 삭제)
    post.unregister();

    postService.save(post);

    return messageConverter.toResponse("게시글이 삭제되었습니다.");
  }

  public MessageResponse update(UserDTO userDTO, PostUpdateRequest req) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    Post post = postService.getByIdAndStatus(req.getId(), PostStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    validatePostAuthor(user, post);

    post.updateInfo(req.getTitle(), req.getContent(), req.getPostKind());

    postService.save(post);

    return messageConverter.toResponse("게시글이 수정되었습니다.");
  }

  // 게시글 종류별로 조회 food, exercise
  @Transactional(readOnly = true)
  public List<PostsResponse> getByPostKind(PostKind postKind) {
    List<Post> posts = postService.getListByPostKindAndStatus(postKind, PostStatus.REGISTERED);

    if (posts.isEmpty()) {
      throw new IllegalArgumentException("USER_NOT_FOUND");
    }

    return postConverter.toPostsResponse(posts);
  }

  public PostResponse getByPostId(UserDTO userDTO, Long id) {
    Post post = postService.getById(id)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    // 수정 예정 (같은 사용자가 조회를 해도 조회수가 늘어남)
    post.addViewCount();
    postService.save(post);

    return postConverter.toPostResponse(post);
  }

  private void validatePostAuthor(User user, Post post) {
    Long loginUserId = user.getId(); // 현재 로그인 사용자
    Long postAuthorId = post.getUser().getId(); // 게시글 작성자

    if (!loginUserId.equals(postAuthorId)) {
      throw new IllegalArgumentException("편집 권한이 없습니다.");
    }
  }

  public MessageResponse like(UserDTO userDTO, Long id) {

    Post post = postService.getByIdAndStatus(id, PostStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    post.like();
    postService.save(post);

    return messageConverter.toResponse("좋아요가 눌렸습니다."); // 띄우기 X
  }

  public MessageResponse unLike(UserDTO userDTO, Long id) {

    Post post = postService.getByIdAndStatus(id, PostStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND"));

    post.unLike();
    postService.save(post);

    return messageConverter.toResponse("싫어요가 눌렸습니다."); // 띄우기 X

  }

  @Transactional(readOnly = true)
  public List<SearchPostResponse> search(String title) {
    List<Post> searchPosts = postService.getByTitleAndStatus(title, PostStatus.REGISTERED);

    if (searchPosts.isEmpty()) {
      throw new IllegalArgumentException("POST_NOT_FOUND");
    }

    return postConverter.toSearchPostResponse(searchPosts);

  }
}
