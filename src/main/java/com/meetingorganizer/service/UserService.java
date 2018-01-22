package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.dto.profile.ProfileInfoDto;
import com.meetingorganizer.dto.profile.ProfileMailDto;
import com.meetingorganizer.dto.profile.ProfilePasswordDto;

/**
 * @author Aleksander
 */
public interface UserService {

    boolean isEmailAlreadyTaken(String email);

    boolean passwordMatchesStoredPassword(String password, User user);

    User registerUser(RegistrationFormDto dto);

    void rollbackUserRegistration(User user);

    User saveUserAndFlush(User user);

    User findOne(Long id);

    User findOneByEmail(String email);

    void createVerificationToken(User user, String token);

    void updateUserProfile(User user, ProfileInfoDto dto);

    void updateUserProfile(User user, ProfileMailDto dto);

    void updateUserProfile(User user, ProfilePasswordDto dto);

    VerificationToken getVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String email);
}
