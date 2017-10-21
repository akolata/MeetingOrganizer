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

    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityManagementService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findAuthorityByName(String authorityName) {
        Authority authority = authorityRepository.findByAuthority(authorityName);
        authority = authority == null ? saveAuthority(authorityName) : authority;

        return authority;
    }

    @Override
    public Authority saveAuthority(String authorityName) {
        return authorityRepository.saveAndFlush(new Authority(authorityName));
    }
}
