package com.mirae.DailyBoost.user.domain.controller;

import com.mirae.DailyBoost.global.annotation.LoginUser;
import com.mirae.DailyBoost.global.api.Api;
import com.mirae.DailyBoost.global.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.business.UserBusiness;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserRequest;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserUpdateRequest;
import com.mirae.DailyBoost.user.domain.controller.model.request.VerifyCodeRequest;
import com.mirae.DailyBoost.user.domain.controller.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserBusiness userBusiness;

  @PostMapping("/initInfo")
  @Operation(
      summary = "사용자 헬스 정보 등록",
      description = "회원가입 시 사용자 헬스 정보를 등록합니다."
  )
  public Api<MessageResponse> userInfoInsert(@LoginUser UserDTO userDTO, @RequestBody UserRequest userRequest) {
    return Api.OK(userBusiness.userInfoInsert(userDTO, userRequest));
  }

  @PostMapping("/unregister")
  @Operation(
      summary = "계정 등록 해제",
      description = "로그인 한 사용자의 계정을 등록 해제합니다."
  )
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO) {
    return Api.OK(userBusiness.unregister(userDTO));
  }

  @PostMapping("/update")
  @Operation(
      summary = "사용자 정보 수정",
      description = "자신의 정보를 수정합니다."
  )
  public Api<MessageResponse> update(@LoginUser UserDTO userDTO,
                                     @RequestPart UserUpdateRequest userUpdateRequest,
                                     @RequestPart MultipartFile file) {
    return Api.OK(userBusiness.updateUserInfo(userDTO, userUpdateRequest, file));
  }

  @PostMapping("/recover")
  @Operation(
      summary = "계정 복구",
      description = "사용자의 계정을 다시 복구합니다."
  )
  public Api<MessageResponse> recoverUserAccount(@RequestBody VerifyCodeRequest request) {
    return Api.OK(userBusiness.recoverUserAccount(request));
  }

  @GetMapping("/{userId}")
  @Operation(
      summary = "사용자 프로필 조회",
      description = "사용자의 프로필을 조회합니다."
  )
  public Api<UserResponse> userProfile(@PathVariable Long userId) {
    return Api.OK(userBusiness.getByIdAndStatus(userId));
  }

}
