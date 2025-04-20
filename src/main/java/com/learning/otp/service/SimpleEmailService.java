package com.learning.otp.service;

import com.learning.otp.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Milan Rathod
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SimpleEmailService {

  private final JavaMailSender emailSender;

  /**
   * Method for sending simple e-mail message.
   * Uses SendGrid if enabled, otherwise falls back to Spring Mail.
   *
   * @param emailDTO - emailDto
   */
  public Boolean sendSimpleMessage(EmailDTO emailDTO) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(String.join(",", emailDTO.getRecipients()));
    mailMessage.setSubject(emailDTO.getSubject());
    mailMessage.setText(emailDTO.getBody());

    boolean isSent = false;
    try {
      emailSender.send(mailMessage);
      isSent = true;
    } catch (Exception e) {
      log.error("Sending e-mail error: {}", e.getMessage());
    }
    return isSent;
  }
}
