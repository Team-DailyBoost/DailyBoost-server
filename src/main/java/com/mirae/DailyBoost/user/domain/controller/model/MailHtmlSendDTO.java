package com.mirae.DailyBoost.user.domain.controller.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailHtmlSendDTO {
  private String emailAddr;
}
