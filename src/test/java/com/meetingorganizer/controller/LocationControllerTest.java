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
public class LocationControllerTest {

    private static final String LOCATIONS_URL = "/location";
    private static final String ADD_LOCATION_URL = "/location/add";
    private static final String BROWSE_LOCATIONS_URL = "/location/browse";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private LocationController locationController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void locationController_isNotNull() {
        assertNotNull(locationController);
    }

    @Test
    public void displayLocationsPage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(LOCATIONS_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(LocationController.LOCATIONS_PAGE))
                .andExpect(status().isOk());
    }

    @Test
    public void displayBrowseLocationsPage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(BROWSE_LOCATIONS_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(LocationController.BROWSE_LOCATIONS_PAGE))
                .andExpect(status().isOk());
    }

    @Test
    public void displayAddLocationPage_shouldReturnValidViewName() throws Exception {
        mvc.perform(get(ADD_LOCATION_URL)
                .with(user(TestHelper.sampleUser()))
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(view().name(LocationController.LOCATION_FORM_PAGE))
                .andExpect(status().isOk());
    }

}
