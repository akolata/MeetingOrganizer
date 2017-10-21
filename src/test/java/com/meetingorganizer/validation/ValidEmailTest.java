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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test of custom email validation
 *
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class ValidEmailTest {

    class TestDto {
        @ValidEmail(message = "Message")
        String email;
    }

    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Before
    public void setup() {
        localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void testValidEmail_givenEmailWithoutAt_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("invalid.mail"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);

        for (ConstraintViolation<TestDto> t : constraintViolations) {
            assertEquals(t.getMessage(), "Message");
        }
    }

    @Test
    public void testValidEmail_givenEmailWithNothingAfterAt_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("invalid.mail@"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testValidEmail_givenTooShortDomain_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("invalid.mail@t"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testValidEmail_givenValidEmail_ShouldBeValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("valid.mail@domain.com"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    @Test
    public void testValidEmail_givenNullEmail_ShouldBeInValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(new TestDto());
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    private TestDto prepareDto(String email) {
        TestDto dto = new TestDto();
        dto.email = email;

        return dto;
    }
}
