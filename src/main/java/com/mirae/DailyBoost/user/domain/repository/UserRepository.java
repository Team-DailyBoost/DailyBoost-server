package com.mirae.DailyBoost.user.domain.repository;

import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByIdAndStatus(Long userId, UserStatus userStatus);

  Optional<User> findByEmail(String email);




}
