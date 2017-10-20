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
 * Created by Aleksander on 18.10.2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RegistrationController.class)
public class RegistrationControllerTest {

    private static final String REGISTRATION_URL = "/register";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RegistrationController registrationController;

    @Test
    public void registrationController_shouldNotBeNull(){
        assertNotNull(registrationController);
    }

    @Test
    public void displayRegistrationPage_getRequest_ShouldReturnValidViewNameAndHasDtoInModel() throws Exception {
        mvc.perform(get(REGISTRATION_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(RegistrationController.REGISTRATION_PAGE))
                .andExpect(model().attributeExists("dto"));
    }
}
