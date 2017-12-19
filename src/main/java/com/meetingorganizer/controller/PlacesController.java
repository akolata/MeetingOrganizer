package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/places")
public class PlacesController {

    public static final String PLACES_PAGE = "places/places";
    public static final String ADD_PLACE_PAGE = "places/addPlace";
    public static final String BROWSE_PLACES_PAGE = "places/browsePlaces";

    @GetMapping
    public String displayPlacesPage() {
        return PLACES_PAGE;
    }

    @GetMapping(value = "/browse")
    public String displayBrowsePlacesPage() {
        return BROWSE_PLACES_PAGE;
    }

    @GetMapping(value = "/add")
    public String displayAddPlacePage() {
        return ADD_PLACE_PAGE;
    }
}
