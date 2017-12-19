package com.meetingorganizer.controller;

import com.meetingorganizer.DemoApplication;
import com.meetingorganizer.config.MeetingOrganizerConfiguration;
import com.meetingorganizer.config.SecurityConfiguration;
import com.meetingorganizer.helpers.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class, SecurityConfiguration.class, MeetingOrganizerConfiguration.class})
public class PlacesControllerTest {

    private static final String PLACES_URL = "/places";
    private static final String ADD_PLACE_URL = "/places/add";
    private static final String BROWSE_PLACES_URL = "/places/browse";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private PlacesController placesController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void placesController_isNotNull() {
        assertNotNull(placesController);
    }

    @Test
    public void displayPlacesPage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(PLACES_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(PlacesController.PLACES_PAGE))
                .andExpect(status().isOk());
    }

    @Test
    public void displayBrowsePlacesPage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(BROWSE_PLACES_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(PlacesController.BROWSE_PLACES_PAGE))
                .andExpect(status().isOk());
    }

    @Test
    public void displayAddPlacePage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(ADD_PLACE_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(PlacesController.ADD_PLACE_PAGE))
                .andExpect(status().isOk());
    }

}
