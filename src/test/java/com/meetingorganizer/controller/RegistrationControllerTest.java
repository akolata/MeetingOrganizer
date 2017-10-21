package com.meetingorganizer.controller;

import com.meetingorganizer.config.SecurityConfiguration;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.ValidationErrorMessagesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import utils.TestUtils;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Registration controller tests
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RegistrationController.class)
@Import(SecurityConfiguration.class)
public class RegistrationControllerTest {

    private static final String REGISTRATION_URL = "/register";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ValidationErrorMessagesUtils errorMessagesUtils;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private UserService userService;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void registrationController_shouldNotBeNull(){
        assertNotNull(registrationController);
    }

    @Test
    public void displayRegistrationPage_getRequest_shouldReturnValidViewNameAndHasDtoInModel() throws Exception {
        mvc.perform(get(REGISTRATION_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().attributeExists("dto"));
    }

    @Test
    public void processRegistrationForm_postRequest_statusShouldBeOk() throws Exception {
        mvc.perform(
                post(REGISTRATION_URL).accept(MediaType.TEXT_HTML).with(csrf())
        ).andExpect(status().isOk());
    }

    @Test
    public void processRegistrationForm_allFieldsAreEmpty_allModelFieldsShouldHaveErrors() throws Exception {
        String[] dtoFields = {"firstName", "lastName", "email", "confirmEmail", "password", "confirmPassword"};

        mvc.perform(post(REGISTRATION_URL)
                .accept(MediaType.TEXT_HTML)
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("confirmEmail", "")
                .param("password", "")
                .param("confirmPassword", "")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("dto", dtoFields))
                .andExpect(model().errorCount(16));
    }

    @Test
    public void processRegistrationForm_fieldsAreNotEmptyButHaveWrongSize_shouldHasErrorCodes() throws Exception {
        String[] dtoFields = {"firstName", "lastName", "email", "confirmEmail", "password", "confirmPassword"};

        mvc.perform(post(REGISTRATION_URL)
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(1))
                .param("lastName", TestUtils.createStringWithLength(1))
                .param("email", TestUtils.createStringWithLength(1))
                .param("confirmEmail", TestUtils.createStringWithLength(1))
                .param("password", TestUtils.createStringWithLength(1))
                .param("confirmPassword", TestUtils.createStringWithLength(1))
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("dto", dtoFields))
                .andExpect(model().errorCount(10));
    }

    @Test
    public void processRegistrationForm_passwordsAndEmailsHaveWrongFormat_shouldHasErrorCodes() throws Exception {
        String[] errorFields = {"email", "confirmEmail", "password", "confirmPassword"};

        mvc.perform(post(REGISTRATION_URL)
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(10))
                .param("lastName", TestUtils.createStringWithLength(10))
                .param("email", "aa@")
                .param("confirmEmail", "aa@")
                .param("password", "aaaaaaa1")
                .param("confirmPassword", "aaaaaaa1")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("dto", errorFields))
                .andExpect(model().errorCount(4));
    }

    @Test
    public void processRegistrationForm_passwordsAndEmailsDoNotMatchWithThemselves_shouldHasErrorCodes() throws Exception {
        mvc.perform(post(REGISTRATION_URL)
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(10))
                .param("lastName", TestUtils.createStringWithLength(10))
                .param("email", "valid@mail.com")
                .param("confirmEmail", "valid@mail.comm")
                .param("password", "goodPa$$1")
                .param("confirmPassword", "goodPa$$2")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2));
    }

    //@Test
    public void processRegistrationForm_validForm_shouldNotHasErrors() throws Exception {
        mvc.perform(post(REGISTRATION_URL)
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(10))
                .param("lastName", TestUtils.createStringWithLength(10))
                .param("email", "valid@mail.com")
                .param("confirmEmail", "valid@mail.com")
                .param("password", "goodPa$$1")
                .param("confirmPassword", "goodPa$$1")
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasNoErrors())
                .andExpect(model().errorCount(0));
    }
}
