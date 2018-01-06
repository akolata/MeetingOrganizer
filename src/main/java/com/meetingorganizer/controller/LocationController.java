package com.meetingorganizer.controller;

import com.meetingorganizer.dto.places.AddLocationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/location")
public class LocationController {

    public static final String LOCATIONS_PAGE = "location/locations";
    public static final String ADD_LOCATION_PAGE = "location/addLocation";
    public static final String BROWSE_LOCATIONS_PAGE = "location/browseLocations";

    @GetMapping
    public String displayLocationsPage() {
        return LOCATIONS_PAGE;
    }

    @GetMapping(value = "/browse")
    public String displayBrowseLocationsPage() {
        return BROWSE_LOCATIONS_PAGE;
    }

    @GetMapping(value = "/add")
    public String displayAddLocationPage(Model model) {
        model.addAttribute("dto", new AddLocationDto());
        return ADD_LOCATION_PAGE;
    }

    @PostMapping(value = "/add")
    public String processAddLocationForm(@Valid @ModelAttribute(name = "dto") AddLocationDto dto,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ADD_LOCATION_PAGE;
        }

        return LOCATIONS_PAGE;
    }
}
