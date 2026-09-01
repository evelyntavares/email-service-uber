package com.uber.emailservice.infrastructure.ses;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.uber.emailservice.adapter.EmailSenderGateway;
import com.uber.emailservice.core.exception.EmailServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SesEmailSender implements EmailSenderGateway {

  private final AmazonSimpleEmailService amazonSimpleEmailService;

  @Override
  public void sendEmail(final String to, final String subject, final String body) {
    SendEmailRequest emailRequest =
        new SendEmailRequest()
            .withSource("email@example.com")
            .withDestination(new Destination().withToAddresses(to))
            .withMessage(
                new Message()
                    .withSubject(new Content(subject))
                    .withBody(new Body().withText(new Content(body))));

    try {
      amazonSimpleEmailService.sendEmail(emailRequest);
    } catch (AmazonServiceException ex) {
      throw new EmailServiceException("Error while sending email.", ex);
    }
  }
}
