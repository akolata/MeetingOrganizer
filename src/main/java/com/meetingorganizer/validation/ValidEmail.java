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
 * Custom annotation for email validation
 *
 * @author Aleksander
 */
@Target({ANNOTATION_TYPE, TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmail.CustomEmailValidator.class)
@Documented
public @interface ValidEmail {

    String message() default "Invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomEmailValidator
            implements ConstraintValidator<ValidEmail, String> {

        private Pattern pattern;
        private Matcher matcher;
        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)" +
                "*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

        @Override
        public void initialize(ValidEmail constraintAnnotation) {

        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (email == null) {
                return false;
            }
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }
}

