package com.meetingorganizer.service.impl;

import com.meetingorganizer.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Mail service
 * @author Aleksander
 */
@Service
public class UtilsMailService implements MessageSourceAware, MailService {

    @Value("${spring.mail.username}")
    private String mailAccount;

    private MessageSource messageSource;

    private JavaMailSender mailSender;

    @Autowired
    public UtilsMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public SimpleMailMessage prepareRegistrationMailMessage(String applicationUrl, String to, Locale locale){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String mailText = messageSource.getMessage("registrationPage.emailMessage", new Object[]{applicationUrl}, locale);
        String mailSubject = messageSource.getMessage("registrationPage.emailSubject", null, locale);

        mailMessage.setTo(to);
        mailMessage.setSubject(mailSubject);
        mailMessage.setText(mailText);
        mailMessage.setFrom(mailAccount);

        return mailMessage;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void sendEmail(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }
}
