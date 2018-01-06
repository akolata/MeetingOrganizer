package com.meetingorganizer.service;

import com.meetingorganizer.domain.Location;

public interface LocationService {

    boolean existsByNameIgnoreCase(String name);

    Location saveAndFlush(Location location);
}
