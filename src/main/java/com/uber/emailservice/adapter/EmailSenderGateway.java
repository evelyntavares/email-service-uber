package com.uber.emailservice.adapter;

import com.uber.emailservice.core.exception.EmailServiceException;

public interface EmailSenderGateway {

  void sendEmail(final String to, final String subject, final String body)
      throws EmailServiceException;
}
