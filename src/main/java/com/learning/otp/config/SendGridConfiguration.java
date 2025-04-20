package com.learning.otp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SendGrid configuration properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.sendgrid")
public class SendGridConfiguration {
    private String apiKey;
    private boolean enabled;
}
