package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Authority;
import com.meetingorganizer.repository.AuthorityRepository;
import com.meetingorganizer.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aleksander
 */
@Service
public class AuthorityManagementService implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority findAuthorityByNameCreateAuthorityIfNotFound(String authorityName) {
        Authority authority = authorityRepository.findByAuthority(authorityName);
        authority = (authority == null) ? saveAuthority(authorityName) : authority;

        return authority;
    }

    @Override
    public Authority saveAuthority(String authorityName) {
        return authorityRepository.saveAndFlush(new Authority(authorityName));
    }
}
