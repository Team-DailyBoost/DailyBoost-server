package com.mirae.DailyBoost.user.domain.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyCodeRequest {
  private String email;
  private String inputCode;

}
