package com.meetingorganizer.dto;

import com.meetingorganizer.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto user for resent token form
 * @author Aleksander
 */
@Getter @Setter
public class ResendTokenDto {

    @ValidEmail
    private String email;

    @Override
    public String toString() {
        return "ResendTokenDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
