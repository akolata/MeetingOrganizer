package com.meetingorganizer.controller;

import com.meetingorganizer.domain.Location;
import com.meetingorganizer.dto.location.AddLocationDto;
import com.meetingorganizer.dto.location.EditLocationDto;
import com.meetingorganizer.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/location")
public class LocationController {

    public static final String LOCATIONS_PAGE = "location/locations";
    public static final String LOCATION_FORM_PAGE = "location/locationForm";
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

        List<Location> locations = locationService.findAll();
        model.addAttribute("locations", locations);

        return BROWSE_LOCATIONS_PAGE;
    }

    @GetMapping(value = "/add")
    public String displayAddLocationPage(Model model) {
        model.addAttribute("dto", new AddLocationDto());
        model.addAttribute("mode", "add");

        return LOCATION_FORM_PAGE;
    }

    @PostMapping(value = "/add")
    public String processAddLocationForm(@Valid @ModelAttribute(name = "dto") AddLocationDto dto,
                                         BindingResult bindingResult, Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "add");
            return LOCATION_FORM_PAGE;
        }

        Location location = new Location(dto);
        locationService.addNewLocation(location, principal.getName());

        return LOCATIONS_PAGE;
    }

    @GetMapping(value = "/{id}/details")
    public String displayLocationDetailsPage(Model model, @PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Location location = locationService.findOneById(id);

        if (location != null) {
            model.addAttribute("location", location);
            model.addAttribute("canEdit", locationService.canEditLocation(location, principal.getName()));
        } else {
            redirectAttributes.addFlashAttribute("displayLocationNotFound", Boolean.TRUE);
            return "redirect:/location";
        }

        return LOCATION_DETAILS_PAGE;
    }

    @GetMapping(value = "/{id}/edit")
    public String displayEditLocationPage(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        Location location = locationService.findOneById(id);

        if (location != null) {
            model.addAttribute("dto", new EditLocationDto(location));
            model.addAttribute("mode", "edit"); // TODO: change modes to enums
        } else {
            redirectAttributes.addFlashAttribute("displayLocationNotFound", Boolean.TRUE);
            return "redirect:/location";
        }
        return LOCATION_FORM_PAGE;
    }

    @PostMapping(value = "/{id}/edit")
    public String processEditLocationForm(@Valid @ModelAttribute(name = "dto") EditLocationDto dto, BindingResult bindingResult,
                                          @PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            return LOCATION_FORM_PAGE;
        }

        Location location = locationService.findOneById(id);

        if(!locationService.isLocationNameAvailable(dto.getName(), location)) {
            model.addAttribute("mode", "edit");
            model.addAttribute("NAME_TAKEN", Boolean.TRUE);
            return LOCATION_FORM_PAGE;
        }

        if (location != null) {
            location.updateFromDto(dto);
            locationService.saveAndFlush(location);
        } else {
            redirectAttributes.addFlashAttribute("displayLocationNotFound", Boolean.TRUE);
            return "redirect:/location";
        }

        // TODO: Add message about successful edition
        return "redirect:/location/" + id + "/details";
    }
}
