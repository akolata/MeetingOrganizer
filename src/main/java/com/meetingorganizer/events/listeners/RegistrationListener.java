package com.meetingorganizer.events.listeners;

import com.meetingorganizer.controller.RegistrationController;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.events.RegistrationCompleteEvent;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;
/**
 * Listener for user's successful registration event.
 * Sends an email with verification token needed for enabling user's account
 */
@Component
public class RegistrationListener
implements ApplicationListener<RegistrationCompleteEvent>, MessageSourceAware{

    @Value("${spring.mail.username}")
    private String from;

    private UserService userService;

    private JavaMailSender mailSender;

    private MessageSource messageSource;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = (User) event.getSource();
        String url = event.getApplicationUrl();
        String token = UUID.randomUUID().toString();

        url += RegistrationController.REGISTRATION_CONFIRM_ENDPOINT;
        url += ("?token=" + token);

        userService.createVerificationToken(user, token);

        SimpleMailMessage mailMessage = prepareMailMessage(url, user.getEmail(), event.getLocale());
        mailSender.send(mailMessage);
    }

    private SimpleMailMessage prepareMailMessage(String applicationUrl, String to, Locale locale){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String mailText = messageSource.getMessage("registrationPage.emailMessage", new Object[]{applicationUrl}, locale);
        String mailSubject = messageSource.getMessage("registrationPage.emailSubject", null, locale);

        mailMessage.setTo(to);
        mailMessage.setSubject(mailSubject);
        mailMessage.setText(mailText);
        mailMessage.setFrom(from);

        return mailMessage;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
