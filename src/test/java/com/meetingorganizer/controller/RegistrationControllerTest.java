package com.meetingorganizer.controller;

import com.meetingorganizer.DemoApplication;
import com.meetingorganizer.config.MeetingOrganizerConfiguration;
import com.meetingorganizer.config.SecurityConfiguration;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.service.MailService;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Registration controller tests
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, SecurityConfiguration.class, MeetingOrganizerConfiguration.class})
public class RegistrationControllerTest {

    private static final String REGISTRATION_URL = "/register";
    private static final String CONFIRM_TOKEN_URL = "/register/confirm";
    private static final String RESEND_TOKEN_URL = "/register/resendToken";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private UserService usersService;

    @MockBean
    private MailService mailService;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

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
                post(REGISTRATION_URL).with(csrf()).accept(MediaType.TEXT_HTML)
        ).andExpect(status().isOk());
    }

    @Test
    public void processRegistrationForm_allFieldsAreEmpty_allModelFieldsShouldHaveErrors() throws Exception {
        String[] dtoFields = {"firstName", "lastName", "email", "confirmEmail", "password", "confirmPassword"};

        mvc.perform(post(REGISTRATION_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("confirmEmail", "")
                .param("password", "")
                .param("confirmPassword", "")
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

    @Test
    public void processRegistrationForm_validForm_shouldNotHasErrors() throws Exception {
        mvc.perform(post(REGISTRATION_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(10))
                .param("lastName", TestUtils.createStringWithLength(10))
                .param("email", "valid@mail.com")
                .param("confirmEmail", "valid@mail.com")
                .param("password", "goodPa$$1")
                .param("confirmPassword", "goodPa$$1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasNoErrors())
                .andExpect(model().errorCount(0));
    }

    @Test
    public void processRegistrationForm_validFormButMailIsTaken_shouldHasErrorAttribute() throws Exception {
        when(usersService.isEmailAlreadyTaken(any(String.class))).thenReturn(true);

        mvc.perform(post(REGISTRATION_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("firstName", TestUtils.createStringWithLength(10))
                .param("lastName", TestUtils.createStringWithLength(10))
                .param("email", "valid@mail.com")
                .param("confirmEmail", "valid@mail.com")
                .param("password", "goodPa$$1")
                .param("confirmPassword", "goodPa$$1")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("emailAlreadyTaken"));

        verify(usersService, times(1)).isEmailAlreadyTaken("valid@mail.com");
    }

    @Test
    public void confirmRegistration_tokenNotFound_modelShouldHasFlashAttribute() throws Exception {
        String token = "token";

        when(usersService.getVerificationToken(token)).thenReturn(null);

        mvc.perform(get(CONFIRM_TOKEN_URL)
                .accept(MediaType.TEXT_HTML)
                .param("token", token)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("tokenNotFound"));
    }

    @Test
    public void confirmRegistration_tokenExpired_modelShouldHasFlashAttribute() throws Exception {
        String token = "token";
        VerificationToken verificationToken = Mockito.mock(VerificationToken.class);

        when(usersService.getVerificationToken(token)).thenReturn(verificationToken);
        when(verificationToken.isTokenExpired()).thenReturn(true);

        mvc.perform(get(CONFIRM_TOKEN_URL)
                .accept(MediaType.TEXT_HTML)
                .param("token", token)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("tokenExpired"));
    }

    @Test
    public void confirmRegistration_tokenValid_modelShouldHasFlashAttribute() throws Exception {
        String token = "token";
        VerificationToken verificationToken = Mockito.mock(VerificationToken.class);
        User userFromToken = Mockito.mock(User.class);

        when(usersService.getVerificationToken(token)).thenReturn(verificationToken);
        when(verificationToken.isTokenExpired()).thenReturn(false);
        when(verificationToken.getUser()).thenReturn(userFromToken);

        mvc.perform(get(CONFIRM_TOKEN_URL)
                .accept(MediaType.TEXT_HTML)
                .param("token", token)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("userEnabled"));

        verify(userFromToken, times(1)).setEnabled(true);
    }

    @Test
    public void displayResentTokenPage_modelShouldHasDtoAndStatusShouldBeOk() throws Exception {
        mvc.perform(get(RESEND_TOKEN_URL)
                .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("resendToken"))
                .andExpect(model().attributeExists("dto"));
    }

    @Test
    public void processResentTokenForm_formHasErrors_shouldReturnToForm() throws Exception {
        String invalidMail = "invalid.mail";

        mvc.perform(post(RESEND_TOKEN_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("email", invalidMail)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.RESEND_TOKEN_PAGE))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(1));
    }

    @Test
    public void processResentTokenForm_mailNotRegistered_shouldReturnToForm() throws Exception {
        String mail = "valid@mail.com";

        when(usersService.isEmailAlreadyTaken(mail)).thenReturn(false);

        mvc.perform(post(RESEND_TOKEN_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("email", mail)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.RESEND_TOKEN_PAGE))
                .andExpect(model().attributeExists("emailNotRegistered"));
    }

    @Test
    public void processResentTokenForm_sendMailException_shouldReturnToForm() throws Exception {
        String mail = "valid@mail.com";
        VerificationToken verificationToken = Mockito.mock(VerificationToken.class);
        User user = Mockito.mock(User.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        when(usersService.isEmailAlreadyTaken(mail)).thenReturn(true);
        when(usersService.generateNewVerificationToken(mail)).thenReturn(verificationToken);
        when(verificationToken.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(mail);
        when(mailService.prepareRegistrationMailMessage(any(String.class), any(String.class), any(Locale.class)))
                .thenReturn(mailMessage);
        doThrow(new RuntimeException()).when(mailService).sendEmail(mailMessage);

        mvc.perform(post(RESEND_TOKEN_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("email", mail)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.RESEND_TOKEN_PAGE))
                .andExpect(model().attributeExists("emailSendException"));
    }

    @Test
    public void processResentTokenForm_formIsOk_ShouldRedirectToLoginAndShowMessage() throws Exception {
        String mail = "valid@mail.com";
        VerificationToken verificationToken = Mockito.mock(VerificationToken.class);
        User user = Mockito.mock(User.class);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        when(usersService.isEmailAlreadyTaken(mail)).thenReturn(true);
        when(usersService.generateNewVerificationToken(mail)).thenReturn(verificationToken);
        when(verificationToken.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(mail);
        when(mailService.prepareRegistrationMailMessage(any(String.class), any(String.class), any(Locale.class)))
                .thenReturn(mailMessage);
        doNothing().when(mailService).sendEmail(mailMessage);

        mvc.perform(post(RESEND_TOKEN_URL)
                .with(csrf())
                .accept(MediaType.TEXT_HTML)
                .param("email", mail)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("tokenResend"));
    }
}
