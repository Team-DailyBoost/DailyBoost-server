package com.mirae.DailyBoost.user.domain.service;

import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.UserRepository;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User save(User user) {

    return userRepository.save(user);
  }

  public Optional<User> getByStatus(Long id, UserStatus status) {
    return userRepository.findUserByIdAndStatus(id, status);
  }

  public Optional<User> getByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public List<User> getListByEmails(List<String> emails) {
    return userRepository.findAllByEmailIn(emails);
  }


  public Optional<User> getById(Long id) {
    return userRepository.findById(id);
  }

}
