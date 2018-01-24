package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Location;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.repository.LocationRepository;
import com.meetingorganizer.service.LocationService;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationManagementService implements LocationService {

    private LocationRepository locationRepository;
    private UserService userService;

    @Autowired
    public LocationManagementService(LocationRepository locationRepository, UserDetailsService userDetailsService, UserService userService) {
        this.locationRepository = locationRepository;
        this.userService = userService;
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return locationRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Location addNewLocation(Location location, String creatorEmail) {
        location.setCreatedBy(userService.findOneByEmail(creatorEmail));
        return locationRepository.saveAndFlush(location);
    }

    @Override
    public Location saveAndFlush(Location location) {
        return locationRepository.saveAndFlush(location);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findOneById(Long id) {
        return locationRepository.findOne(id);
    }

    @Override
    public boolean canEditLocation(Location location, String userEmail) {
        User currentUser = userService.findOneByEmail(userEmail);
        return currentUser == null ? false : location.getCreatedBy() == currentUser;
    }

    @Override
    public boolean isLocationNameAvailable(String name, Location currentLocation) {
        boolean available = false;

        if (isNameTaken(name) || isNameSameAsBeforeEdition(currentLocation.getName(), name)) {
            available = true;
        }

        return available;
    }

    private boolean isNameTaken(String name) {
        return locationRepository.countAllByNameIgnoreCase(name) <= 0;
    }

    private boolean isNameSameAsBeforeEdition(String oldName, String editedName) {
        return editedName.equalsIgnoreCase(oldName);
    }
}
