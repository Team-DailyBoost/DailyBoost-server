package com.mirae.DailyBoost.user.domain.business;

import com.mirae.DailyBoost.global.annotation.Business;
import com.mirae.DailyBoost.global.converter.MessageConverter;
import com.mirae.DailyBoost.global.errorCode.UserErrorCode;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.OAuthAttributes;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserRequest;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserUpdateRequest;
import com.mirae.DailyBoost.user.domain.controller.model.request.VerifyCodeRequest;
import com.mirae.DailyBoost.user.domain.controller.model.response.UserResponse;
import com.mirae.DailyBoost.user.domain.converter.UserConverter;
import com.mirae.DailyBoost.user.domain.repository.User;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import com.mirae.DailyBoost.user.domain.service.RecoveryCodeStore;
import com.mirae.DailyBoost.user.domain.service.RecoveryCodeStore.VerifyResult;
import com.mirae.DailyBoost.user.domain.service.UserService;
import com.mirae.DailyBoost.user.exception.user.UserNotFoundException;
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
  private final RecoveryCodeStore recoveryCodeStore;


  public User register(OAuthAttributes attributes) {

    User user = userConverter.toEntity(attributes);

    return userService.save(user);
  }

  public MessageResponse unregister(UserDTO userDTO) {
    if (userDTO.getRole() == null) {
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    User user = userService.getByEmail(userDTO.getEmail())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    if (user.getStatus() == UserStatus.UNREGISTERED) {
      throw new IllegalArgumentException("계정이 이미 삭제된 상태입니다.");
    }

    user.changeStatus(UserStatus.UNREGISTERED); // register -> unregister
    user.initUnregisterAt(LocalDateTime.now());

    userService.save(user);

    return messageConverter.toResponse("계정이 삭제되었습니다."); // 한달 간 저장 후 영구 삭제

  }

  public MessageResponse recoverUserAccount(VerifyCodeRequest request) {

    User user = userService.getByEmailAndStatusNot(request.getEmail(), UserStatus.REGISTERED)
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    VerifyResult result = recoveryCodeStore.verifyAndConsume(request.getEmail(),
        request.getInputCode());

    switch (result) {
      case SUCCESS -> {}
      case MISMATCH -> throw new IllegalArgumentException("VERIFICATION_CODE_MISMATCH");
      case EXPIRED_OR_NOT_FOUND -> throw new IllegalArgumentException("VERIFICATION_CODE_EXPIRED_OR_NOT_FOUND");
    }

    user.changeStatus(UserStatus.REGISTERED);
    user.initUnregisterAt(null);
    userService.save(user);

    return messageConverter.toResponse("계정이 복구 되었습니다.");
  }

  public MessageResponse updateUserInfo(UserDTO userDTO, UserUpdateRequest request) {

    User user = userService.getById(userDTO.getId())
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    user.updateInfo(request.getAge(), request.getGender(), request.getHealthInfo());

    userService.save(user);

    return messageConverter.toResponse("프로필이 수정되었습니다.");

  }

  public MessageResponse userInfoInsert(UserDTO userDTO, UserRequest userRequest) {

    if (userDTO == null || userDTO.getId() == null || userDTO.getEmail() == null) {
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }

    User user = userService.getByStatus(userDTO.getId(), UserStatus.REGISTERED)
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    user.initInfo(userRequest.getHealthInfo());

    userService.save(user);

    return messageConverter.toResponse("정보가 입력되었습니다.");

  }

  public User getByEmailElseRegister(String email, OAuthAttributes attributes) {
    return userService.getByEmail(email)
        .orElseGet(() -> register(attributes));
  }


  @Transactional(readOnly = true)
  public UserResponse getByIdAndStatus(Long id) {
    User user = userService.getByIdAndStatus(id, UserStatus.REGISTERED)
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    return userConverter.toUserResponse(user);
  }

  @Transactional(readOnly = true)
  public User getByStatus(Long id, UserStatus status) {
    return userService.getByStatus(id, status)
        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

  }

}
