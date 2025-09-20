package com.mirae.DailyBoost.user.domain.controller;

import com.mirae.DailyBoost.common.annotation.LoginUser;
import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.oauth.dto.UserDTO;
import com.mirae.DailyBoost.user.domain.business.UserBusiness;
import com.mirae.DailyBoost.user.domain.controller.model.request.UserRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserBusiness userBusiness;
  private final HttpSession session;

  @PostMapping("/initInfo")
  public Api<MessageResponse> userInfoInsert(@LoginUser UserDTO userDTO, @RequestBody UserRequest userRequest) {
    log.info("현재 세션 ID: {}", session.getId());
    log.info("세션 user 속성: {}", session.getAttribute("user"));
    return Api.OK(userBusiness.userInfoInsert(userDTO, userRequest));
  }

  @PostMapping("/unregister")
  public Api<MessageResponse> unregister(@LoginUser UserDTO userDTO) {
    return Api.OK(userBusiness.unregister(userDTO));
  }

  @PostMapping("/revoke")
  public Api<MessageResponse> revokeUnregister(@LoginUser UserDTO userDTO) {
    return Api.OK(userBusiness.revokeUnregistration(userDTO));
  }


}
