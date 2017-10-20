package com.meetingorganizer.dto;


import com.meetingorganizer.validation.FieldsValueMatch;
import com.meetingorganizer.validation.ValidEmail;
import com.meetingorganizer.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Aleksander
 * Registration DTO used while user registration
 * IMPORTANT : because of problems with error codes naming for class-level annotations,
 * those codes are read later and read from message bundle.
 * To make it work, {@link com.meetingorganizer.validation.FieldsValueMatch} message property should be name
 * same as property in message bundle
 */
@Getter @Setter
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "FieldsValueMatch.passwordsNotMatch"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "FieldsValueMatch.emailsNotMatch"
        )
})
public class RegistrationFormDto {

    @NotBlank
    @Size(min = 2)
    private String firstName;

    @NotBlank
    @Size(min = 2)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 100)
    @ValidEmail
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    @ValidEmail
    private String confirmEmail;

    @NotBlank
    @Size(min = 8)
    @ValidPassword
    private String password;

    @NotBlank
    @Size(min = 8)
    @ValidPassword
    private String confirmPassword;

    @Override
    public String toString() {
        return "RegistrationFormDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", verifyEmail='" + confirmEmail + '\'' +
                ", password='" + password + '\'' +
                ", verifyPassword='" + confirmPassword + '\'' +
                '}';
    }
}
