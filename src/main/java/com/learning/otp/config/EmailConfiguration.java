package com.learning.otp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Milan Rathod
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class EmailConfiguration {
  private String from;
}
