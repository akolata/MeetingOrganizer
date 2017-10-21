package com.meetingorganizer.service;

import org.springframework.mail.SimpleMailMessage;

import java.util.Locale;

/**
 * @author Aleksander
 */
public interface MailService {

    void sendEmail(SimpleMailMessage mailMessage);

    SimpleMailMessage prepareRegistrationMailMessage(String applicationUrl, String to, Locale locale);
}
