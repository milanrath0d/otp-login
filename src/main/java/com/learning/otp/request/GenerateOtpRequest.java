package com.learning.otp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Milan Rathod
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateOtpRequest {
  private String userName;
  private String phoneNumber;
  private String email;
}
