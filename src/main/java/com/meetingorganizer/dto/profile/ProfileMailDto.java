package com.meetingorganizer.dto.profile;

import com.meetingorganizer.validation.FieldsValueMatch;
import com.meetingorganizer.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * DTO for user's mail edition form
 * @author Aleksander
 */
@Getter @Setter
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "FieldsValueMatch.emailsNotMatch"
        )
})
public class ProfileMailDto {

    @NotBlank
    @Size(min = 3, max = 100)
    @ValidEmail
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    @ValidEmail
    private String confirmEmail;

    public ProfileMailDto() {}

    @Override
    public String toString() {
        return "ProfileMailDto{" +
                ", email='" + email + '\'' +
                ", confirmEmail='" + confirmEmail + '\'' +
                '}';
    }
}
