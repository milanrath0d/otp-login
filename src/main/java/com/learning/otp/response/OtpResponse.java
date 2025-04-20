package com.learning.otp.response;

import com.learning.otp.model.OtpStatus;
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
public class OtpResponse {
  private String message;
  private OtpStatus status;
}
