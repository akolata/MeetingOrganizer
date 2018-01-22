package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;

public interface TokenService {

    VerificationToken saveAndFlush(VerificationToken token);

    VerificationToken findByUser(User user);

    VerificationToken findByToken(String token);

    void delete(VerificationToken token);
}
