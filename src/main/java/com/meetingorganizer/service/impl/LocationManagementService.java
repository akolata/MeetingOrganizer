package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Location;
import com.meetingorganizer.repository.LocationRepository;
import com.meetingorganizer.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationManagementService implements LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationManagementService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return locationRepository.existsByNameIgnoreCase(name);
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
}
