package com.uber.emailservice.core.usecase;

import com.uber.emailservice.adapter.EmailSenderGateway;
import com.uber.emailservice.core.port.SendEmail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendEmailUseCase implements SendEmail {

  private EmailSenderGateway emailSenderGateway;

  @Override
  public void execute(final String to, final String subject, final String body) {
    emailSenderGateway.sendEmail(to, subject, body);
  }
}
