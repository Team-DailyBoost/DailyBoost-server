package com.mirae.DailyBoost.post.domain.post.repository;

import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostKind;
import com.mirae.DailyBoost.post.domain.post.repository.enums.PostStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Boolean existsByUser_IdAndTitleAndStatus(Long userId, String title, PostStatus status);
  List<Post> findAllByPostKind(PostKind postKind);

  List<Post> findAllByPostKindAndStatus(PostKind postKind, PostStatus status);

  Boolean existsByIdAndCommentsContains(Long id, Comment comment);

  Optional<Post> findByIdAndStatus(Long id, PostStatus status);

  @Query(
      "SELECT p FROM Post p WHERE p.title LIKE %:title%"
  )
  List<Post> findByTitleAndStatus(String title, PostStatus status);
}
