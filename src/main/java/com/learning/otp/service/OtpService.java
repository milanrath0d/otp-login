package com.learning.otp.service;

import com.learning.otp.config.SendGridConfiguration;
import com.learning.otp.config.TwilioConfiguration;
import com.learning.otp.dto.EmailDTO;
import com.learning.otp.model.OtpStatus;
import com.learning.otp.request.GenerateOtpRequest;
import com.learning.otp.request.ValidateOtpRequest;
import com.learning.otp.response.OtpResponse;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Milan Rathod
 */
@Service
@Slf4j
@AllArgsConstructor
public class OtpService {
  private final SimpleEmailService simpleEmailService;
  private final SendGridEmailService sendGridEmailService;
  private final OTPGeneratorService otpGeneratorService;
  private final TwilioConfiguration twilioConfiguration;
  private final SendGridConfiguration sendGridConfiguration;

  public Mono<OtpResponse> sendSmsOtp(GenerateOtpRequest generateOtpRequest) {
    PhoneNumber to = new PhoneNumber(generateOtpRequest.getPhoneNumber());
    PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());

    String otp = otpGeneratorService.generateOTP(generateOtpRequest.getUserName());
    if (otp == null) {
      log.error("OTP generator is not working...");
      return Mono.error(Exception::new);
    }

    String otpMessage = "Dear Customer , Your OTP is " + otp + ". Use this Passcode to login.";
    Message.creator(to, from, otpMessage).create();

    OtpResponse otpResponse = OtpResponse.builder().message(otpMessage).status(OtpStatus.DELIVERED).build();

    return Mono.just(otpResponse);
  }

  /**
   * Send OTP via email
   *
   * @param generateOtpRequest containing username and email
   * @return OTP response with status
   */
  public Mono<OtpResponse> sendEmailOtp(GenerateOtpRequest generateOtpRequest) {
    String otp = otpGeneratorService.generateOTP(generateOtpRequest.getUserName());
    if (otp == null) {
      log.error("OTP generator is not working...");
      return Mono.error(Exception::new);
    }

    EmailDTO emailDTO = createOtpEmailDTO(generateOtpRequest.getEmail(), otp);
    
    // Choose the appropriate email service based on configuration
    Boolean emailSent = sendEmailWithConfiguredService(emailDTO);
    
    if (emailSent) {
      String message = "OTP sent to your email address: " + generateOtpRequest.getEmail();
      log.info("Email OTP sent successfully to: {}", generateOtpRequest.getEmail());
      return Mono.just(OtpResponse.builder()
              .message(message)
              .status(OtpStatus.DELIVERED)
              .build());
    } else {
      log.error("Failed to send email OTP to: {}", generateOtpRequest.getEmail());
      return Mono.just(OtpResponse.builder()
              .message("Failed to send OTP via email. Please try again or use another method.")
              .status(OtpStatus.FAILED)
              .build());
    }
  }

  /**
   * Creates an EmailDTO object for OTP email
   * 
   * @param emailAddress recipient's email address
   * @param otp the one-time password to send
   * @return configured EmailDTO object
   */
  private EmailDTO createOtpEmailDTO(String emailAddress, String otp) {
    List<String> recipients = new ArrayList<>();
    recipients.add(emailAddress);

    EmailDTO emailDTO = new EmailDTO();
    emailDTO.setSubject("Your OTP for Authentication");
    emailDTO.setBody("Dear Customer, Your OTP is " + otp + ". Use this Passcode to login.");
    emailDTO.setRecipients(recipients);
    emailDTO.setHtml(false);
    
    return emailDTO;
  }
  
  /**
   * Sends email using the configured email service (SendGrid or JavaMail)
   * 
   * @param emailDTO the email to send
   * @return true if email was sent successfully
   */
  private Boolean sendEmailWithConfiguredService(EmailDTO emailDTO) {
    if (sendGridConfiguration.isEnabled()) {
      log.info("Using SendGrid email service");
      return sendGridEmailService.sendEmail(emailDTO);
    } else {
      log.info("Using default JavaMail email service");
      return simpleEmailService.sendSimpleMessage(emailDTO);
    }
  }

  /**
   * Method for validating provided OTP
   *
   * @param request - validateOtpRequest
   * @return boolean value (true|false)
   */
  public Mono<String> validateOtp(ValidateOtpRequest request) {
    String cacheOTP = otpGeneratorService.getOPTByKey(request.getUserName());
    if (cacheOTP != null && cacheOTP.equals(request.getOneTimePassword())) {
      otpGeneratorService.clearOTPFromCache(request.getUserName());
      return Mono.just("OTP validation successful");
    }
    return Mono.error(new IllegalArgumentException("Invalid otp please retry !"));
  }
}
