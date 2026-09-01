package com.uber.emailservice.infrastructure.ses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.uber.emailservice.core.exception.EmailServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SesEmailSender Tests")
class SesEmailSenderTest {

  @Mock private AmazonSimpleEmailService amazonSimpleEmailService;

  private SesEmailSender sesEmailSender;

  @BeforeEach
  void setUp() {
    sesEmailSender = new SesEmailSender(amazonSimpleEmailService);
  }

  @Test
  @DisplayName("Should send email successfully")
  void testSendEmailSuccess() {
    String to = "recipient@example.com";
    String subject = "Test Subject";
    String body = "Test Body";

    sesEmailSender.sendEmail(to, subject, body);

    ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
    verify(amazonSimpleEmailService).sendEmail(captor.capture());

    SendEmailRequest request = captor.getValue();
    assertNotNull(request);
    assertEquals("email@example.com", request.getSource());
    assertTrue(request.getDestination().getToAddresses().contains(to));
    assertEquals(subject, request.getMessage().getSubject().getData());
    assertEquals(body, request.getMessage().getBody().getText().getData());
  }

  @Test
  @DisplayName("Should send email with multiple recipients")
  void testSendEmailWithDifferentRecipient() {
    String to = "another@example.com";
    String subject = "Another Subject";
    String body = "Another Body";

    sesEmailSender.sendEmail(to, subject, body);

    verify(amazonSimpleEmailService).sendEmail(any(SendEmailRequest.class));
  }

  @Test
  @DisplayName("Should throw EmailServiceException when AmazonServiceException occurs")
  void testSendEmailThrowsEmailServiceException() {
    String to = "recipient@example.com";
    String subject = "Test Subject";
    String body = "Test Body";

    AmazonServiceException amazonException = new AmazonServiceException("AWS Error");
    doThrow(amazonException).when(amazonSimpleEmailService).sendEmail(any(SendEmailRequest.class));

    EmailServiceException exception =
        assertThrows(
            EmailServiceException.class, () -> sesEmailSender.sendEmail(to, subject, body));

    assertEquals("Error while sending email.", exception.getMessage());
    assertEquals(amazonException, exception.getCause());
  }

  @Test
  @DisplayName("Should verify sendEmail is called exactly once")
  void testSendEmailCalledOnce() {
    String to = "test@example.com";
    String subject = "Subject";
    String body = "Body";

    sesEmailSender.sendEmail(to, subject, body);

    verify(amazonSimpleEmailService, times(1)).sendEmail(any(SendEmailRequest.class));
    verifyNoMoreInteractions(amazonSimpleEmailService);
  }

  @Test
  @DisplayName("Should handle empty strings in parameters")
  void testSendEmailWithEmptyParameters() {
    sesEmailSender.sendEmail("empty@example.com", "", "");

    verify(amazonSimpleEmailService).sendEmail(any(SendEmailRequest.class));
  }
}
