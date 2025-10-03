package com.mirae.DailyBoost.user.domain.business;

import com.mirae.DailyBoost.common.converter.MessageConverter;
import com.mirae.DailyBoost.common.model.MessageResponse;
import com.mirae.DailyBoost.user.domain.controller.model.MailHtmlSendDTO;
import com.mirae.DailyBoost.user.domain.repository.enums.UserStatus;
import com.mirae.DailyBoost.user.domain.service.RecoveryCodeStore;
import com.mirae.DailyBoost.user.domain.service.UserService;
import jakarta.mail.IllegalWriteException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MailSendBusiness {
  private final UserService userService;
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final RecoveryCodeStore recoveryCodeStore;
  private final MessageConverter messageConverter;

  /**
   * 텍스트 기반 메일 전송
   *
   * @param mailTextSendDTO
   */
//  @Override
//  public void sendTextEmail(MailTextSendDTO mailTextSendDTO) {
//    SimpleMailMessage smm = new SimpleMailMessage();
//    smm.setTo(mailTextSendDTO.getEmailAddr());
//    smm.setSubject(mailTextSendDTO.getSubject());
//    smm.setText(mailTextSendDTO.getContent());
//
//    try{
//      mailSender.send(smm);
//      log.info("이메일 전송 성공!");
//    } catch (MailException e) {
//      log.info("이메일 전송중에 오류가 발생했습니다. {} ", e.getMessage());
//      throw e;
//    }
//  }

  /**
   * html/text 기반 메일 전송
   *
   * @param mailHtmlSendDTO
   * @throws MessagingException
   * @throws IOException
   */
  public MessageResponse sendHtmlMessage(MailHtmlSendDTO mailHtmlSendDTO)
      throws MessagingException, IOException {

    Boolean isExistsUser = userService.existsByEmailAndStatusNot(mailHtmlSendDTO.getEmailAddr(),
        UserStatus.REGISTERED);

    if (!isExistsUser) {
      throw new IllegalArgumentException("이 계정은 정상적인 계정입니다.");
    }

    try {
      mailSender.createMimeMessage();
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      int n = ThreadLocalRandom.current().nextInt(0, 10000);
      String code = String.format("%04d", n);
      recoveryCodeStore.saveCode(mailHtmlSendDTO.getEmailAddr(), code, Duration.ofMinutes(5));

      if (!recoveryCodeStore.underSendLimit(mailHtmlSendDTO.getEmailAddr()))
        throw new IllegalWriteException("TOO_FREQUENT_REQUEST");

      Context context = new Context();
      context.setVariable("EXPIRE_MINUTES", "5");
      context.setVariable("CODE", code);
      context.setVariable("RECIPIENT_EMAIL", mailHtmlSendDTO.getEmailAddr());

      String base64Image = getBase64EncodedImage("static/images/Daily-Boost.png");
      context.setVariable("logoImage", base64Image);

      String htmlContent = templateEngine.process("email-template", context);
      helper.setTo(mailHtmlSendDTO.getEmailAddr());
      helper.setSubject("DailyBoost: 계정 복구 인증 코드");
      helper.setText(htmlContent, true);
      mailSender.send(message);
      log.info("Thymeleaf 템플릿 이메일 전송 성공!");
    } catch (MessagingException e) {
      log.info("Thymeleaf 템플릿 이메일 전송 중 오류 발생: {}", e.getMessage());
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return messageConverter.toResponse("이메일이 전송되었습니다.");
  }

  private String getBase64EncodedImage(String imagePath) throws IOException {
    Resource resource = new ClassPathResource(imagePath);
    byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
    return Base64.getEncoder().encodeToString(bytes);
  }
}
