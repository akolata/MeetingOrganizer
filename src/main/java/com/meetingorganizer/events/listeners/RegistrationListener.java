package com.meetingorganizer.events.listeners;

import com.meetingorganizer.controller.RegistrationController;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.events.RegistrationCompleteEvent;
import com.meetingorganizer.service.MailService;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;
/**
 * Listener for user's successful registration event.
 * Sends an email with verification token needed for enabling user's account
 */
@Component
public class RegistrationListener
implements ApplicationListener<RegistrationCompleteEvent>{

    private UserService userService;

    private JavaMailSender mailSender;

    private MailService mailService;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender, MailService mailService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = (User) event.getSource();
        String url = event.getTokenConfirmationUrl();
        String token = UUID.randomUUID().toString();

        url += RegistrationController.REGISTRATION_CONFIRM_ENDPOINT;
        url += ("?token=" + token);

        userService.createVerificationToken(user, token);

        SimpleMailMessage mailMessage = mailService.prepareRegistrationMailMessage(url,
                user.getEmail(), event.getLocale());
        mailSender.send(mailMessage);
    }

}
