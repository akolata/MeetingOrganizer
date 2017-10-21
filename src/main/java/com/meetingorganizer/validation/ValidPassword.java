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
 * Custom annotation for password strength validation
 *
 * @author Aleksander
 */
@Target({ANNOTATION_TYPE, TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPassword.CustomPasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "At least one capital letter,digit and special character, no whitespaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomPasswordValidator
            implements ConstraintValidator<ValidPassword, String> {

        private Pattern pattern;
        private Matcher matcher;
        private final static String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        @Override
        public void initialize(ValidPassword constraintAnnotation) {

        }

        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {

            if (password == null) {
                return false;
            }

            pattern = Pattern.compile(PASSWORD_REGEX);
            matcher = pattern.matcher(password);

            return matcher.matches();
        }
    }
}
