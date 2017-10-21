package com.meetingorganizer.validation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Test for custom validation annotations responsible for check if two fields of a class are equal
 *
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class FieldsValueMatchTest {

    @FieldsValueMatch.List({
            @FieldsValueMatch(field = "email", fieldMatch = "confirmEmail")
    })
    class TestDto {
        String email;
        String confirmEmail;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getConfirmEmail() {
            return confirmEmail;
        }

        public void setConfirmEmail(String confirmEmail) {
            this.confirmEmail = confirmEmail;
        }
    }

    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Before
    public void setup() {
        localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void testFieldsValueMatch_givenDifferentValues_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("invalid.mail", "valid.mail"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testFieldsValueMatch_givenNullValues_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("invalid.mail", null));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testFieldsValueMatch_givenBothNullValues_ShouldBeValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto(null, null));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    @Test
    public void testFieldsValueMatch_givenEqualValues_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("valid.mail", "valid.mail"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    private TestDto prepareDto(String email, String confirmEmail) {
        TestDto dto = new TestDto();
        dto.email = email;
        dto.confirmEmail = confirmEmail;

        return dto;
    }
}
