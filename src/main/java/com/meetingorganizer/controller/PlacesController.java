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
    public String displayAddPlacePage(Model model) {
        model.addAttribute("dto", new AddLocationDto());
        return ADD_PLACE_PAGE;
    }

    @PostMapping(value = "/add")
    public String processAddPlaceForm(@Valid @ModelAttribute(name = "dto") AddLocationDto dto,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ADD_PLACE_PAGE;
        }

        return PLACES_PAGE;
    }
}
