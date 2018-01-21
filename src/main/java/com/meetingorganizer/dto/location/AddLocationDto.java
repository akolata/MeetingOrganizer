package com.meetingorganizer.dto.location;

import com.meetingorganizer.validation.UniqueLocationName;
import org.hibernate.validator.constraints.NotBlank;

public class AddLocationDto extends LocationDto {

    @NotBlank
    @UniqueLocationName
    private String name;

    public AddLocationDto() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AddLocationDto{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }

}
