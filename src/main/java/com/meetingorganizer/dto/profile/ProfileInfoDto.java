package com.meetingorganizer.dto.profile;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.validation.ValidPhone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Dto for basic profile's info validation
 *
 * @author Aleksander
 */
@Getter
@Setter
public class ProfileInfoDto {

    @NotBlank
    @Size(min = 2)
    private String firstName;

    @NotBlank
    @Size(min = 2)
    private String lastName;

    @ValidPhone
    private String phone;

    public ProfileInfoDto() {
    }

    public ProfileInfoDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
    }

    @Override
    public String toString() {
        return "ProfileInfoDto{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
