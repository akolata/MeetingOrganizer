package com.meetingorganizer.controller;

import com.meetingorganizer.DemoApplication;
import com.meetingorganizer.config.MeetingOrganizerConfiguration;
import com.meetingorganizer.config.SecurityConfiguration;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, SecurityConfiguration.class, MeetingOrganizerConfiguration.class})
public class ImageControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ImageController imageController;

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
    public void imageController_isNotNull() {
        assertNotNull(imageController);
    }

    @Test
    public void getUsersProfileImage_userNotFound_emptyResponse() throws Exception {
        when(userService.findOne(any(Long.class))).thenReturn(null);

        MvcResult mvcResult = mvc.perform(get("/profile/{id}/image", 1)
                .with(user(new User())))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @Test
    public void getUsersProfileImage_userWithoutImage_emptyResponse() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userService.findOne(user.getId())).thenReturn(user);

        MvcResult mvcResult = mvc.perform(get("/profile/{id}/image", user.getId())
                .with(user(new User())))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, mvcResult.getResponse().getContentLength());
    }

    @Test
    public void getUsersProfileImage_uploadImage_validResponse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setProfilePicture("picture".getBytes());

        when(userService.findOne(user.getId())).thenReturn(user);

        MvcResult mvcResult = mvc.perform(get("/profile/{id}/image", user.getId())
                .with(user(new User())))
                .andExpect(status().isOk())
                .andReturn();

        assertArrayEquals("picture".getBytes(), mvcResult.getResponse().getContentAsByteArray());
    }
}
