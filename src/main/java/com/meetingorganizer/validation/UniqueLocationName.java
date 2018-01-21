package com.meetingorganizer.validation;

import com.meetingorganizer.service.LocationService;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ANNOTATION_TYPE, TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLocationName.CustomLocationNameValidator.class)
@Documented
public @interface UniqueLocationName {

    String message() default "This location name is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomLocationNameValidator
            implements ConstraintValidator<UniqueLocationName, String> {

        private LocationService locationService;

        public CustomLocationNameValidator(LocationService locationService) {
            this.locationService = locationService;
        }

        @Override
        public void initialize(UniqueLocationName constraintAnnotation) {
        }

        @Override
        public boolean isValid(String locationName, ConstraintValidatorContext context) {
            return !locationService.existsByNameIgnoreCase(locationName);
        }
    }
}
