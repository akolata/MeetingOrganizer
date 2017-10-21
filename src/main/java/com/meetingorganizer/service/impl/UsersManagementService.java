package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Authority;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.repository.UserRepository;
import com.meetingorganizer.service.AuthorityService;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aleksander on 21.10.2017.
 */
@Service
public class UsersManagementService implements UserService {

    private UserRepository userRepository;
    private AuthorityService authorityService;

    @Autowired
    public UsersManagementService(UserRepository userRepository, AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
    }

    @Override
    public boolean isEmailAlreadyTaken(String email) {
        Long usersWithEqualEmail = userRepository.countAllByEmail(email);
        return usersWithEqualEmail > 0;
    }

    @Override
    public User saveUser(RegistrationFormDto dto) {
        User user = new User(dto);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityService.findAuthorityByName("USER"));
        user.setAuthorities(authorities);

        return userRepository.saveAndFlush(user);
    }
}
