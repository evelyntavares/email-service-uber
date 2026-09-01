package com.uber.emailservice.core.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.uber.emailservice.adapter.EmailSenderGateway;
import com.uber.emailservice.core.exception.EmailServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendEmailUseCase Tests")
class SendEmailUseCaseTest {
  @Mock private EmailSenderGateway emailSenderGateway;

  private SendEmailUseCase sendEmailUseCase;

  @BeforeEach
  void setUp() {
    sendEmailUseCase = new SendEmailUseCase(emailSenderGateway);
  }

  @Test
  @DisplayName("Should execute email sending successfully")
  void testExecuteEmailSuccess() {
    String to = "recipient@example.com";
    String subject = "Test Subject";
    String body = "Test Body";

    sendEmailUseCase.execute(to, subject, body);

    verify(emailSenderGateway, times(1)).sendEmail(to, subject, body);
  }

  @Test
  @DisplayName("Should propagate exception from gateway")
  void testExecutePropagatesException() {
    String to = "recipient@example.com";
    String subject = "Test Subject";
    String body = "Test Body";

    EmailServiceException exception = new EmailServiceException("Gateway error");
    doThrow(exception).when(emailSenderGateway).sendEmail(to, subject, body);

    assertThrows(EmailServiceException.class, () -> sendEmailUseCase.execute(to, subject, body));
    verify(emailSenderGateway).sendEmail(to, subject, body);
  }
}
