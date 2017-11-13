package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.Authority;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.dto.profile.ProfileInfoDto;
import com.meetingorganizer.dto.profile.ProfileMailDto;
import com.meetingorganizer.dto.profile.ProfilePasswordDto;
import com.meetingorganizer.repository.UserRepository;
import com.meetingorganizer.repository.VerificationTokenRepository;
import com.meetingorganizer.service.AuthorityService;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Service for managing user's entities
 */
@Service
public class UsersManagementService implements UserService {

    private UserRepository userRepository;
    private AuthorityService authorityService;
    private VerificationTokenRepository tokenRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsersManagementService(UserRepository userRepository,
                                  AuthorityService authorityService,
                                  VerificationTokenRepository tokenRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isEmailAlreadyTaken(String email) {
        Long usersWithEqualEmail = userRepository.countAllByEmail(email);
        return usersWithEqualEmail > 0;
    }

    @Override
    public User registerUser(RegistrationFormDto dto) {
        User user = new User(dto);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityService.findAuthorityByNameCreateAuthorityIfNotFound("USER"));

        user.setAuthorities(authorities);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.saveAndFlush(verificationToken);
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public User saveUserAndFlush(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String email) {
        User user = userRepository.findByEmail(email);
        VerificationToken actualToken = tokenRepository.findByUser(user);

        actualToken.setToken(UUID.randomUUID().toString());
        actualToken.updateExpirationTime();

        actualToken = tokenRepository.saveAndFlush(actualToken);
        return actualToken;
    }

    @Override
    public boolean passwordMatchesStoredPassword(String password, User user) {
        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void updateUserProfile(User user, ProfileInfoDto dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());

        saveUserAndFlush(user);
    }

    @Override
    public void updateUserProfile(User user, ProfileMailDto dto) {
        user.setEmail(dto.getEmail());
        saveUserAndFlush(user);
    }

    @Override
    public void updateUserProfile(User user, ProfilePasswordDto dto) {
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        saveUserAndFlush(user);
    }

}
