package com.meetingorganizer.dto.location;

import com.meetingorganizer.validation.UniqueLocationName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ToString
@Getter
@Setter
public class AddLocationDto {

    @NotBlank
    @UniqueLocationName
    private String name;

    @NotBlank
    private String description;

    @Min(value = 1)
    @Max(value = 300)
    private int maxMembers;

}
