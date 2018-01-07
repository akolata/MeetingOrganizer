package com.meetingorganizer.controller;

import com.meetingorganizer.domain.Location;
import com.meetingorganizer.dto.location.AddLocationDto;
import com.meetingorganizer.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/location")
public class LocationController {

    public static final String LOCATIONS_PAGE = "location/locations";
    public static final String ADD_LOCATION_PAGE = "location/addLocation";
    public static final String BROWSE_LOCATIONS_PAGE = "location/browseLocations";
    public static final String LOCATION_DETAILS_PAGE = "location/details";

    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public String displayLocationsPage() {
        return LOCATIONS_PAGE;
    }

    @GetMapping(value = "/browse")
    public String displayBrowseLocationsPage(Model model) {
        model.addAttribute("locations", locationService.findAll());
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

        Location location = new Location(dto);
        locationService.saveAndFlush(location);

        return LOCATIONS_PAGE;
    }

    @GetMapping(value = "/{id}/details")
    public String displayLocationDetailsPage(Model model, @PathVariable Long id) {
        model.addAttribute("location", locationService.findOneById(id));

        return LOCATION_DETAILS_PAGE;
    }
}
