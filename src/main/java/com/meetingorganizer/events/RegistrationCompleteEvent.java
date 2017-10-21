package com.meetingorganizer.events;

import com.meetingorganizer.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Event published when user will successfully register
 * @author Aleksander
 */
@Getter @Setter
public class RegistrationCompleteEvent
extends ApplicationEvent{

    private User user;
    private String tokenConfirmationUrl;
    private Locale locale;

    /**
     * Create new event and pass parameters needed for email message
     * @param source registered user's object
     * @param tokenConfirmationUrl url of currently working application
     * @param locale needed for messages properties
     */
    public RegistrationCompleteEvent(Object source, String tokenConfirmationUrl, Locale locale) {
        super(source);

        this.user = (User) source;
        this.tokenConfirmationUrl = tokenConfirmationUrl;
        this.locale = locale;
    }
}
