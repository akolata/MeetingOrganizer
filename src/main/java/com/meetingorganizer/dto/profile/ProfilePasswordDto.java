package com.meetingorganizer.dto.profile;

import com.meetingorganizer.validation.FieldsValueMatch;
import com.meetingorganizer.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * DTO for user's password edition form
 * @author Aleksander
 */
@Getter @Setter
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "FieldsValueMatch.passwordsNotMatch"
        )
})
public class ProfilePasswordDto {

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

    public ProfilePasswordDto() {
    }

    @Override
    public String toString() {
        return "ProfilePasswordDto{" +
                ", oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
