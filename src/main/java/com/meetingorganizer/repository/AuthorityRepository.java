package com.meetingorganizer.repository;

import com.meetingorganizer.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aleksander
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthority(String authority);
}
