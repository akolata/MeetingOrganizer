package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Location;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.repository.LocationRepository;
import com.meetingorganizer.service.LocationService;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LocationManagementService implements LocationService {

    private int latestPageSize = PageWrapper.DEFAULT_PAGE_SIZE;
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
    public Location saveAndFlush(Location location, String creatorEmail) {
        location.setCreatedBy(userService.findOneByEmail(creatorEmail));
        return locationRepository.saveAndFlush(location);
    }

    @Override
    public Page<Location> findAll(int page, int pageSize) {

        if (latestPageSize != pageSize) {
            page = 1;
        }

        latestPageSize = pageSize;
        return locationRepository.findAll(new PageRequest(
                PageWrapper.adjustPageNumber(page) - 1,
                PageWrapper.adjustPageSize(pageSize)));
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
}
