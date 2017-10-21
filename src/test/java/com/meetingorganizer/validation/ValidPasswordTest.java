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
 * Test of custom password validation
 *
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class ValidPasswordTest {

    class TestDto {
        @ValidPassword(message = "Message")
        String password;
    }

    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Before
    public void setup() {
        localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void testValidPassword_givenToShortPassword_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("aZ1#"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);

        for (ConstraintViolation<TestDto> t : constraintViolations) {
            assertEquals(t.getMessage(), "Message");
        }
    }

    @Test
    public void testValidPassword_givenNullPassword_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(new TestDto());
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testValidPassword_givenNoSpecialCharacter_ShouldBeInvalid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("abcABC12"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 1);
    }

    @Test
    public void testValidPassword_givenCorrectPassword_ShouldBeValid() {
        Set<ConstraintViolation<TestDto>> constraintViolations =
                localValidatorFactoryBean.validate(prepareDto("abcABC1$"));
        constraintViolations = new HashSet<>(constraintViolations);

        assertTrue(constraintViolations.size() == 0);
    }

    private TestDto prepareDto(String password) {
        TestDto dto = new TestDto();
        dto.password = password;

        return dto;
    }
}
