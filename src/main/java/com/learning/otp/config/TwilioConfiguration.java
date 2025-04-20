package com.learning.otp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Milan Rathod
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfiguration {
  private String accountSid;
  private String authToken;
  private String trialNumber;
} 