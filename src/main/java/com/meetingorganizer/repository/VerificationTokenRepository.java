package com.meetingorganizer.repository;

import com.meetingorganizer.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aleksander
 */
public interface VerificationTokenRepository
extends JpaRepository<VerificationToken, Long>{

    VerificationToken findByToken(String token);
}
