package com.learning.otp.service;

import com.learning.otp.config.EmailConfiguration;
import com.learning.otp.config.SendGridConfiguration;
import com.learning.otp.dto.EmailDTO;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service for sending emails via SendGrid
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SendGridEmailService {

    private final SendGridConfiguration sendGridConfiguration;
    private final EmailConfiguration emailConfiguration;

    /**
     * Sends an email using SendGrid API
     *
     * @param emailDTO - email details
     * @return true if the email was sent successfully, false otherwise
     */
    public Boolean sendEmail(EmailDTO emailDTO) {
        try {
            SendGrid sendGrid = new SendGrid(sendGridConfiguration.getApiKey());
            
            Email from = new Email(emailConfiguration.getFrom());
            String subject = emailDTO.getSubject();
            
            // Create mail
            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setSubject(subject);
            
            // Set content (HTML or plain text)
            Content content;
            if (emailDTO.isHtml()) {
                content = new Content("text/html", emailDTO.getBody());
            } else {
                content = new Content("text/plain", emailDTO.getBody());
            }
            mail.addContent(content);
            
            // Add recipients
            Personalization personalization = new Personalization();
            for (String recipient : emailDTO.getRecipients()) {
                personalization.addTo(new Email(recipient));
            }
            
            // Add CC if present
            if (emailDTO.getCcList() != null && !emailDTO.getCcList().isEmpty()) {
                for (String cc : emailDTO.getCcList()) {
                    personalization.addCc(new Email(cc));
                }
            }
            
            // Add BCC if present
            if (emailDTO.getBccList() != null && !emailDTO.getBccList().isEmpty()) {
                for (String bcc : emailDTO.getBccList()) {
                    personalization.addBcc(new Email(bcc));
                }
            }
            
            mail.addPersonalization(personalization);
            
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sendGrid.api(request);
            
            log.info("SendGrid response code: {}", response.getStatusCode());
            
            return response.getStatusCode() >= 200 && response.getStatusCode() < 300;
        } catch (IOException e) {
            log.error("Failed to send email via SendGrid: {}", e.getMessage(), e);
            return false;
        }
    }
}
