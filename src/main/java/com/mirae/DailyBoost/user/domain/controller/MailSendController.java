package com.mirae.DailyBoost.user.domain.controller;

import com.mirae.DailyBoost.common.api.Api;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.user.domain.controller.model.MailHtmlSendDTO;
import com.mirae.DailyBoost.user.domain.business.MailSendBusiness;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class MailSendController {

  private final MailSendBusiness mailSendBusiness;

//  @PostMapping("/txtEmail")
//  public ResponseEntity<Object> sendTxtEmail(@RequestBody MailTextSendDTO mailTextSendDTO) {
//    mailSendServiceImpl.sendTextEmail(mailTextSendDTO);
//    return new ResponseEntity<>(null, HttpStatus.OK);
//  }

  @PostMapping("/htmlEmail")
  @Operation(
      summary = "이메일 전송",
      description = "지정된 이메일에 계정 복구 코드를 전송합니다."
  )
  public Api<MessageResponse> sendHtmlEmail(@RequestBody MailHtmlSendDTO mailHtmlSendDTO)
      throws MessagingException, IOException {
    return Api.OK(mailSendBusiness.sendHtmlMessage(mailHtmlSendDTO));
  }
}
