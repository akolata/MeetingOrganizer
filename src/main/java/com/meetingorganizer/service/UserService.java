package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.dto.RegistrationFormDto;

/**
 * Created by Aleksander on 21.10.2017.
 */
public interface UserService {

    boolean isEmailAlreadyTaken(String email);

    User saveUser(RegistrationFormDto dto);
}
