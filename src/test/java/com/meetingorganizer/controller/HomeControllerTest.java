package com.meetingorganizer.controller;

import com.meetingorganizer.DemoApplication;
import com.meetingorganizer.config.MeetingOrganizerConfiguration;
import com.meetingorganizer.config.SecurityConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests for HomeController
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, SecurityConfiguration.class, MeetingOrganizerConfiguration.class})
public class HomeControllerTest {

    private static final String HOME_URL = "/home";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private HomeController homeController;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void homeController_isNotNull() {
        assertNotNull(homeController);
    }

    @Test
    public void displayHomePage_returnsValidViewName() throws Exception {
        mvc.perform(get(HOME_URL)
                .with(user("user")))
                .andExpect(status().isOk())
                .andExpect(view().name(HomeController.HOME_PAGE));
    }
}
