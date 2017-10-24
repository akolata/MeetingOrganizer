package com.meetingorganizer.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Login controller tests
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LoginController.class, secure = false)
public class LoginControllerTest {

    private static final String LOGIN_URL = "/login";

    @Autowired
    private LoginController loginController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void loginController_shouldNotBeNull() {
        assertNotNull(loginController);
    }

    @Test
    public void redirectToLogin_ShouldRedirectToLogin() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void displayLoginPage_GetRequest_ShouldReturnValidViewName() throws Exception {
        this.mvc.perform(get(LOGIN_URL))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
