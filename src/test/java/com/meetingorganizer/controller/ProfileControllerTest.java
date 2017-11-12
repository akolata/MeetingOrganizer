package com.meetingorganizer.controller;

import com.meetingorganizer.DemoApplication;
import com.meetingorganizer.config.MeetingOrganizerConfiguration;
import com.meetingorganizer.config.SecurityConfiguration;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.dto.profile.ProfileInfoDto;
import com.meetingorganizer.dto.profile.ProfileMailDto;
import com.meetingorganizer.dto.profile.ProfilePasswordDto;
import com.meetingorganizer.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, SecurityConfiguration.class, MeetingOrganizerConfiguration.class})
public class ProfileControllerTest {

    private static final String PROFILE_URL = "/profile";
    private static final String EDIT_PROFILE_URL = "/profile/edit";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ProfileController profileController;

    @MockBean
    private UserService userService;


    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void profileController_isNotNull() {
        assertNotNull(profileController);
    }

    @Test
    public void displayProfilePage_returnValidViewNameAndHasModelAttribute() throws Exception {
        mvc.perform(
                get(PROFILE_URL).with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(ProfileController.USER_ATTRIBUTE))
                .andExpect(view().name(ProfileController.PROFILE_PAGE));
    }

    @Test
    public void uploadProfileImage_shouldCallService() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "orig",
                MediaType.IMAGE_JPEG_VALUE, "image".getBytes());

        mvc.perform(fileUpload(PROFILE_URL).file(file)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.MULTIPART_FORM_DATA_VALUE)).andExpect(status().isOk());

        verify(userService, times(1)).saveUserAndFlush(any(User.class));
    }

    @Test
    public void displayEditProfilePage_shouldReturnValidViewNameAndHasModelAttributes() throws Exception {
        mvc.perform(
                get(EDIT_PROFILE_URL).with(user(new User())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        ProfileController.PROFILE_INFO_DTO,
                        ProfileController.PROFILE_MAIL_DTO,
                        ProfileController.PROFILE_PASSWORD_DTO
                ))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));
    }

    @Test
    public void processEditInfoForm_formValid_shouldCallService() throws Exception {

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editInfo", "editInfo")
                .param("firstName", "John")
                .param("lastName", "Smith")
                .param("phone", "+48 103 234 567"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("updateSuccessful"))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(1)).updateUserProfile(any(User.class), any(ProfileInfoDto.class));
    }

    @Test
    public void processEditInfoForm_formInvalid_shouldHasErrors() throws Exception {

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editInfo", "editInfo")
                .param("firstName", "")
                .param("lastName", "")
                .param("phone", "abc"))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(5))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(0)).updateUserProfile(any(User.class), any(ProfileInfoDto.class));
    }

    @Test
    public void processEditMailForm_formValidButMailTaken_shouldNotCallService() throws Exception {
        when(userService.isEmailAlreadyTaken(any(String.class))).thenReturn(true);

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editMail", "")
                .param("email", "new@domain.com")
                .param("confirmEmail", "new@domain.com"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("emailAlreadyTaken"))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(0)).updateUserProfile(any(User.class), any(ProfileMailDto.class));
    }

    @Test
    public void processEditMailForm_formInvalid_shouldHasErrors() throws Exception {
        when(userService.isEmailAlreadyTaken(any(String.class))).thenReturn(true);

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editMail", "")
                .param("email", "new@domain.com")
                .param("confirmEmail", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(4)) // 3 for confirm mail, and emails not match
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(0)).updateUserProfile(any(User.class), any(ProfileMailDto.class));
    }

    @Test
    public void processEditMailForm_formValid_shouldCallService() throws Exception {
        when(userService.isEmailAlreadyTaken(any(String.class))).thenReturn(false);

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editMail", "")
                .param("email", "new@domain.com")
                .param("confirmEmail", "new@domain.com"))
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("updateSuccessful"))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(1)).updateUserProfile(any(User.class), any(ProfileMailDto.class));
    }

    @Test
    public void processEditPasswordForm_formInvalid_shouldHasErrors() throws Exception {

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editPassword", "")
                .param("oldPassword", "")
                .param("password", "asd")
                .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(9))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(0)).updateUserProfile(any(User.class), any(ProfilePasswordDto.class));
    }

    @Test
    public void processEditPasswordForm_formInvalidButOldPassIncorrect_shouldHasErrors() throws Exception {
        when(userService.passwordMatchesStoredPassword(any(String.class), any(User.class))).thenReturn(false);

        mvc.perform(post(EDIT_PROFILE_URL)
                .with(csrf())
                .with(user(sampleUser()))
                .accept(MediaType.TEXT_HTML)
                .param("editPassword", "")
                .param("oldPassword", "abcD123@")
                .param("password", "abcD123@")
                .param("confirmPassword", "abcD123@"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPasswordNotEqual"))
                .andExpect(view().name(ProfileController.EDIT_PROFILE_PAGE));

        verify(userService, times(0)).updateUserProfile(any(User.class), any(ProfilePasswordDto.class));
    }

    private User sampleUser() {
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setEmail("mail@domain.com");
        user.setPassword("pass");

        return user;
    }
}
