package com.learning.otp.web.handler;

import com.learning.otp.request.GenerateOtpRequest;
import com.learning.otp.request.ValidateOtpRequest;
import com.learning.otp.service.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Milan Rathod
 */
@Component
@Slf4j
@AllArgsConstructor
public class OtpLoginHandler {

  private final OtpService otpService;

  public Mono<ServerResponse> sendOtp(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(GenerateOtpRequest.class)
      .flatMap(request -> {
        // Log complete request details for debugging
        log.info("Received OTP request: userName={}, phoneNumber={}, email={}",
                request.getUserName(),
                request.getPhoneNumber(),
                request.getEmail());
                
        // Check if email field is provided
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
          log.info("Sending OTP via email to: {}", request.getEmail());
          return otpService.sendEmailOtp(request);
        } 
        // Otherwise it's a regular SMS OTP
        else {
          log.info("Sending OTP via SMS to: {}", request.getPhoneNumber());
          return otpService.sendSmsOtp(request);
        }
      })
      .flatMap(otpResponse -> ServerResponse.status(HttpStatus.OK)
        .body(BodyInserters.fromValue(otpResponse)));
  }

  public Mono<ServerResponse> validateOtp(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(ValidateOtpRequest.class)
      .flatMap(otpService::validateOtp)
      .flatMap(response -> ServerResponse.status(HttpStatus.OK)
        .bodyValue(response));
  }
}
