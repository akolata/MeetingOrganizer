package com.meetingorganizer.dto;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by Aleksander on 18.10.2017.
 * Registration DTO used while user registration
 */
@Getter @Setter
public class RegistrationFormDto {

    private String firstName;

    private String lastName;

    private String email;

    private String verifyEmail;

    private String password;

    private String verifyPassword;
}
