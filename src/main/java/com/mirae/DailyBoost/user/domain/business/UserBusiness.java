package com.mirae.DailyBoost.user.domain.business;

import com.mirae.DailyBoost.common.annotation.Business;
import com.mirae.DailyBoost.common.converter.MessageConverter;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.oauth.OAuthAttributes;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserRequest;
import com.mirae.DailyBoost.user.domain.controller.model.response.UserResponse;
import com.mirae.DailyBoost.user.domain.converter.UserConverter;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import com.mirae.DailyBoost.user.domain.service.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Business
@Slf4j
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserBusiness {

  private final UserService userService;
  private final UserConverter userConverter;
  private final MessageConverter messageConverter;

  public User register(OAuthAttributes attributes) {

    User user = userConverter.toEntity(attributes);

    return userService.save(user);
  }

  public MessageResponse unregister(UserDTO userDTO) {
    if (userDTO.getRole() == null) {
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    User user = userService.getByEmail(userDTO.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    if (user.getStatus() == UserStatus.UNREGISTERED) {
      throw new IllegalArgumentException("계정이 이미 삭제된 상태입니다.");
    }

    user.changeStatus(UserStatus.UNREGISTERED); // register -> unregister
    user.initUnregisterAt(LocalDateTime.now());

    userService.save(user);

    return messageConverter.toResponse("계정이 삭제되었습니다."); // 한달 간 저장 후 영구 삭제

  }

  public MessageResponse recoverUserAccount(UserDTO userDTO) {

    return null;
  }

  public MessageResponse revokeUnregistration(UserDTO userDTO) {

    if (userDTO.getRole() == null) {
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    User user = userService.getByEmail(userDTO.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    user.changeStatus(UserStatus.REGISTERED); // unregistered -> registered
    user.initUnregisterAt(null);

    userService.save(user);

    return messageConverter.toResponse("계정이 복구 되었습니다.");

  }

  public MessageResponse userInfoInsert(UserDTO userDTO, UserRequest userRequest) {

    if (userDTO == null || userDTO.getId() == null || userDTO.getEmail() == null) {
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    log.info("=============={}, {}, {} ============", userDTO.getId(), userDTO.getEmail(), userDTO.getRole());

    User user = userService.getByStatus(userDTO.getId(), UserStatus.REGISTERED)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    user.initInfo(userRequest.getHealthInfo(), userRequest.getGoal(), userRequest.getAllergy());

    userService.save(user);

    return messageConverter.toResponse("정보가 입력되었습니다.");

  }

  public User getByEmailElseRegister(String email, OAuthAttributes attributes) {
    return userService.getByEmail(email).orElseGet(
        () -> register(attributes)
    );
  }

  // ADMIN 전용
  public UserResponse getByEmail(UserDTO userDTO) {
    User user = userService.getByEmail(userDTO.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    return userConverter.toUserResponse(user);
  }

  @Transactional(readOnly = true)
  public User getByStatus(Long id, UserStatus status) {
    return userService.getByStatus(id, status)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

  }

}
