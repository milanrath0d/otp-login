package com.learning.otp.web.router;

import com.learning.otp.web.handler.OtpLoginHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Milan Rathod
 */
@Configuration
@AllArgsConstructor
public class OtpLoginRouter {

  private final OtpLoginHandler handler;

  @Bean
  public RouterFunction<ServerResponse> handleSMS() {
    return RouterFunctions.route()
      .POST("/router/sendOTP", handler::sendOtp)
      .POST("/router/validateOTP", handler::validateOtp)
      .build();
  }
}
