package com.meetingorganizer.dto.location;

import com.meetingorganizer.domain.Location;
import org.hibernate.validator.constraints.NotBlank;

public class EditLocationDto extends LocationDto {

    @NotBlank
    private String name;

    public EditLocationDto() {
    }

    public EditLocationDto(Location location) {
        super(location);
        this.name = location.getName();
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
        return "EditLocationDto{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
