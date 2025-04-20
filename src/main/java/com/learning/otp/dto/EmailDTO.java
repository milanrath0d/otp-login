package com.learning.otp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Milan Rathod
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
  private String subject;
  private String body;
  private boolean isHtml;
  private String attachmentPath;
  private List<String> recipients;
  private List<String> ccList;
  private List<String> bccList;
}
