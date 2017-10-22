package com.meetingorganizer.service;

import com.meetingorganizer.service.impl.UtilsMailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class UtilsMailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UtilsMailService mailService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendEmail_shouldCallMailSender() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailService.sendEmail(mailMessage);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void prepareRegistrationMailMessage_ShouldCreateCorrectMessage() {
        String url = "http://localhost:8080";
        String to = "user@mail.com";
        Locale locale = Locale.getDefault();
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        when(messageSource.getMessage(anyString(), isNull(Object[].class), any(Locale.class))).thenReturn("subject");
        when(messageSource.getMessage(anyString(), isNotNull(Object[].class), any(Locale.class))).thenReturn("text");
        mailService.setMessageSource(messageSource);

        SimpleMailMessage actualMail = mailService.prepareRegistrationMailMessage(url, to, locale);

        assertNotNull(actualMail);
        assertEquals(to, actualMail.getTo()[0]);
        assertEquals("subject", actualMail.getSubject());
        assertEquals("text", actualMail.getText());
    }
}
