package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;

/**
 * @author Aleksander
 */
public interface UserService {

    boolean isEmailAlreadyTaken(String email);

    User registerUser(RegistrationFormDto dto);

    User saveUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String email);
}
