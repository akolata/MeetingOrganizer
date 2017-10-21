package com.meetingorganizer.service;

import com.meetingorganizer.domain.Authority;

/**
 * Created by Aleksander on 21.10.2017.
 */
public interface AuthorityService {

    Authority findAuthorityByName(String authorityName);

    Authority saveAuthority(String authorityName);
}
