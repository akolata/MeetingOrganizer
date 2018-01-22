package com.meetingorganizer.service.impl;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.repository.VerificationTokenRepository;
import com.meetingorganizer.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenManagementService implements TokenService {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public TokenManagementService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public VerificationToken saveAndFlush(VerificationToken token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void delete(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
