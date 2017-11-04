package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.EditProfileDto;
import com.meetingorganizer.dto.RegistrationFormDto;

/**
 * @author Aleksander
 */
public interface UserService {

    boolean isEmailAlreadyTaken(String email);

    boolean isPasswordDifferentThanCurrent(String password, User user);

    User registerUser(RegistrationFormDto dto);

    User saveUserAndFlush(User user);

    User findOne(Long id);

    void createVerificationToken(User user, String token);

    void updateUserProfile(User user, EditProfileDto dto);

    VerificationToken getVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String email);
}
