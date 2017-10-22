package com.meetingorganizer.service;

import com.meetingorganizer.domain.Authority;

/**
 * @author Aleksander
 */
public interface AuthorityService {

    Authority findAuthorityByNameCreateAuthorityIfNotFound(String authorityName);

    Authority saveAuthority(String authorityName);
}
