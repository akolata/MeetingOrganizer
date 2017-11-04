package com.meetingorganizer.dto;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.validation.FieldsValueMatch;
import com.meetingorganizer.validation.ValidEmail;
import com.meetingorganizer.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO for profile's edition
 * @author Aleksander
 */
@Getter
@Setter
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
public class EditProfileDto {

    private Long id;

    @NotBlank
    @Size(min = 2)
    private String firstName;

    @NotBlank
    @Size(min = 2)
    private String lastName;

    @Pattern(regexp = "[\\s\\d+-]+")
    private String phone;

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
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    @ValidPassword
    private String password;

    @NotBlank
    @Size(min = 8)
    @ValidPassword
    private String confirmPassword;

    public EditProfileDto() {}

    public EditProfileDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.confirmEmail = user.getEmail();
    }

    @Override
    public String toString() {
        return "EditProfileDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", confirmEmail='" + confirmEmail + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
