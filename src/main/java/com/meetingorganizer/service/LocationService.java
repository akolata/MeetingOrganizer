package com.meetingorganizer.service;

import com.meetingorganizer.domain.Location;

import java.util.List;

public interface LocationService {

    boolean existsByNameIgnoreCase(String name);

    Location saveAndFlush(Location location);

    List<Location> findAll();

    Location findOneById(Long id);
}
