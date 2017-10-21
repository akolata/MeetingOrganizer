package com.meetingorganizer.repository;

import com.meetingorganizer.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Aleksander on 21.10.2017.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthority(String authority);
}
