package com.mirae.DailyBoost.post.domain.post.service;

import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.post.repository.Post;
import com.mirae.DailyBoost.post.domain.post.repository.PostRepository;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;

  public Post save(Post post) {
    return postRepository.save(post);
  }

  public void delete(Post post) {
    postRepository.delete(post);
  }

  public Optional<Post> getById(Long id) {
    return postRepository.findById(id);
  }

  public Boolean existsByTitle(Long userId, String title, PostStatus status) {
    return postRepository.existsByUser_IdAndTitleAndStatus(userId, title, status);
  }

  public Optional<Post> getByIdAndStatus(Long id, PostStatus status) {
    return postRepository.findByIdAndStatus(id, status);
  }

  public List<Post> getListByPostKindAndStatus(PostKind postKind, PostStatus status) {
    return postRepository.findAllByPostKindAndStatus(postKind, status);
  }

  public List<Post> getByTitleAndStatus(String title, PostStatus status) {
    return postRepository.findByTitleAndStatus(title, status);
  }

  public Boolean existsByComments(Long postId, Comment comment) {
    return postRepository.existsByIdAndCommentsContains(postId, comment);
  }
}
