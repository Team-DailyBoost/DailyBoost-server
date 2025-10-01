package com.mirae.DailyBoost.post.domain.comment.service;

import com.mirae.DailyBoost.post.domain.comment.repository.Comment;
import com.mirae.DailyBoost.post.domain.comment.repository.CommentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public Comment save(Comment comment) {
    return commentRepository.save(comment);
  }

  public Optional<Comment> getById(Long id) {
    return commentRepository.findById(id);
  }

}
