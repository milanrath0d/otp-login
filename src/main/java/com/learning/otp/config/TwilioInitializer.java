package com.learning.otp.config;

import com.twilio.Twilio;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * @author Milan Rathod
 */
@Configuration
@AllArgsConstructor
public class TwilioInitializer {

  private final TwilioConfiguration twilioConfiguration;

  @PostConstruct
  public void initTwilio() {
    Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
  }

} 