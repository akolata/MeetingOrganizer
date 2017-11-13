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
 * Test of custom phone validation
 *
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class ValidPhoneTest {

    class TestDto {
        @ValidPhone(message = "Message")
        String phone;
    }

    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Before
    public void setup() {
        localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void testValidPhone_givenEmptyString_shouldBeValidBecauseItMightBeEmpty() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto(""));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);

        for (ConstraintViolation<TestDto> t : constraintViolations) {
            assertEquals(t.getMessage(), "Message");
        }
    }

    @Test
    public void testValidPassword_givenNumberWithLetters_shouldHasErrors() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("+48 123 abd"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testValidPassword_givenNumberWithSpaces_shouldBeValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("+48 123-456-789"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    @Test
    public void testValidPassword_givenNumberWithoutSpaces_shouldBeValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("+48123456789"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    private TestDto prepareDto(String phone) {
        TestDto dto = new TestDto();
        dto.phone = phone;

        return dto;
    }
}
