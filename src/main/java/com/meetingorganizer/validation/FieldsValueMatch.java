package com.meetingorganizer.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for checking if two properties are equal
 *
 * @author Aleksander
 */
@Constraint(validatedBy = FieldsValueMatch.FieldsValueMatchValidator.class)
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface FieldsValueMatch {

    String message() default "Fields values don't match!";

    String field();

    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }

    class FieldsValueMatchValidator
            implements ConstraintValidator<FieldsValueMatch, Object> {

        private String field;
        private String fieldMatch;

        public void initialize(FieldsValueMatch constraintAnnotation) {
            this.field = constraintAnnotation.field();
            this.fieldMatch = constraintAnnotation.fieldMatch();
        }

        public boolean isValid(Object value,
                               ConstraintValidatorContext context) {

            Object fieldValue = new BeanWrapperImpl(value)
                    .getPropertyValue(field);
            Object fieldMatchValue = new BeanWrapperImpl(value)
                    .getPropertyValue(fieldMatch);


            if (fieldValue != null) {
                return fieldValue.equals(fieldMatchValue);
            } else {
                return fieldMatchValue == null;
            }
        }
    }
}
