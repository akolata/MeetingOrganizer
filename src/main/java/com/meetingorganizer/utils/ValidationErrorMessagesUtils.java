package com.meetingorganizer.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.*;

/**
 * Utils class for validation errors
 *
 * @author Aleksander
 */
@Component
public class ValidationErrorMessagesUtils
        implements MessageSourceAware {

    private MessageSource messageSource;

    private static final List<String> CLASS_LEVEL_VALIDATION_ERROR_CODES = Arrays.asList("FieldsValueMatch");


    /**
     * For given list of global validations errors, check if they are connected with class-level validations
     * If they are, read their validation message from message bundle and add to map
     *
     * @param globalErrors global validation errors
     * @return Map of entries, where key is the name of error, and value is validation message read from properties
     */
    public Map<String, String> errorMessagesForClassLevelValidations(List<ObjectError> globalErrors) {
        Map<String, String> messages = new HashMap<>();

        for (ObjectError globalError : globalErrors) {
            String[] errorCodes = globalError.getCodes();

            for (String errorCode : errorCodes) {
                if (CLASS_LEVEL_VALIDATION_ERROR_CODES.contains(errorCode)) {
                    String errorMessage = globalError.getDefaultMessage();
                    String messageKey = extractMessageKeyNameFromErrorMessage(errorMessage);
                    String messageValue = messageSource.getMessage(errorMessage, null, Locale.getDefault());
                    messages.put(messageKey, messageValue);
                }
            }
        }

        return messages;
    }

    /**
     * Class-level validations has message property as default "AnnotationClass.messageKey"
     * For a given message extract the key out of it
     *
     * @param message validation error default message
     * @return extracted key from validation error
     */
    public String extractMessageKeyNameFromErrorMessage(String message) {
        String[] splitted = message.split("\\.");
        return splitted[1];
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
