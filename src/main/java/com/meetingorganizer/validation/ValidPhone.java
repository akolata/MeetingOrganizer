package com.meetingorganizer.validation;


import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;

/**
 * Custom annotation for phone number validation
 *
 * @author Aleksander
 */
@Target({ANNOTATION_TYPE, TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhone.CustomEmailValidator.class)
@Documented
public @interface ValidPhone {

    String message() default "Invalid phone format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomEmailValidator
            implements ConstraintValidator<ValidPhone, String> {

        private Pattern pattern;
        private Matcher matcher;
        private static final String PHONE_PATTERN = "[\\s\\d+-]+";

        @Override
        public void initialize(ValidPhone constraintAnnotation) {

        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (email == null || email.trim().equals("")) {
                return true;
            }

            pattern = Pattern.compile(PHONE_PATTERN);
            matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }
}

