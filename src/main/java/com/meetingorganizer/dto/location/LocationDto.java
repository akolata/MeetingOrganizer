package com.meetingorganizer.dto.location;

import com.meetingorganizer.domain.Location;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public abstract class LocationDto {

    @NotBlank
    @Getter
    @Setter
    private String description;

    @Min(value = 1)
    @Max(value = 300)
    @Getter
    @Setter
    private int maxMembers;

    public LocationDto() {
    }

    public LocationDto(Location location) {
        this.description = location.getDescription();
        this.maxMembers = location.getMaxMembers();
    }

    public abstract String getName();

    public abstract void setName(String name);

    @Override
    public String toString() {
        return "LocationDto{" +
                "description='" + description + '\'' +
                ", maxMembers=" + maxMembers +
                '}';
    }
}
