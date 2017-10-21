package com.meetingorganizer.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.ObjectError;
import utils.TestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Test messages utils class
 *
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationErrorMessagesUtilsTest {

    @Autowired
    private ValidationErrorMessagesUtils messagesUtils;

    @MockBean
    private MessageSource messageSource;

    @Test
    public void errorMessagesForClassLevelValidations_givenEmptyList_ShouldReturnEmptyMap() {
        assertEquals(Collections.emptyMap(),
                messagesUtils.errorMessagesForClassLevelValidations(Collections.emptyList()));
    }

    @Test
    public void errorMessagesForClassLevelValidations_givenErrorNotConnectedWithClassLevelValidation_ShouldReturnEmptyMap() {
        String[] codes = {TestUtils.createStringWithLength(5)};
        ObjectError error = new ObjectError("name", codes, new Object[]{}, "defaultMsg");

        assertEquals(Collections.emptyMap(), messagesUtils.errorMessagesForClassLevelValidations(Arrays.asList(error)));
    }

    @Test
    public void errorMessagesForClassLevelValidations_givenErrorConnectedWithClassLevelValidation_ShouldReturnValidMap() {
        String[] codes = {"FieldsValueMatch"};
        ObjectError error = new ObjectError("name", codes, new Object[]{},
                "FieldsValueMatch.propertyNotMatch");
        String expectedMessage = "Expected validation message";


        given(messageSource.getMessage("FieldsValueMatch.propertyNotMatch", null, Locale.getDefault()))
                .willReturn(expectedMessage);

        Map<String, String> messages = messagesUtils.errorMessagesForClassLevelValidations(Arrays.asList(error));
        String actualMessage = messages.get("propertyNotMatch");

        assertEquals(1, messages.size());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void extractMessageKeyNameFromErrorMessage_givenValidMessage_shouldReturnValidKey() {
        assertEquals("expected", messagesUtils.extractMessageKeyNameFromErrorMessage("annotationName.expected"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void extractMessageKeyNameFromErrorMessage_givenInvalidFormat_shouldThrowException() {
        assertEquals("expected", messagesUtils.extractMessageKeyNameFromErrorMessage("annotationName"));
    }
}
