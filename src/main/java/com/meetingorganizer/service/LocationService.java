package com.meetingorganizer.service;

import com.meetingorganizer.domain.Location;

import java.util.List;

public interface LocationService {

    boolean existsByNameIgnoreCase(String name);

    Location addNewLocation(Location location, String creatorEmail);

    Location saveAndFlush(Location location);

    List<Location> findAll();

    Location findOneById(Long id);

    boolean canEditLocation(Location location, String userEmail);
}
