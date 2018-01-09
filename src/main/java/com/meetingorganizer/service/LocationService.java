package com.meetingorganizer.service;

import com.meetingorganizer.domain.Location;
import org.springframework.data.domain.Page;

public interface LocationService {

    boolean existsByNameIgnoreCase(String name);

    Location saveAndFlush(Location location);

    Page<Location> findAll(int page, int pageSize);

    Location findOneById(Long id);
}