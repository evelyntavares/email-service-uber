package com.uber.emailservice.core.port;

public interface SendEmail {
  void execute(final String to, final String subject, final String body);
}
