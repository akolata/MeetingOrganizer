package com.meetingorganizer.repository;

import com.meetingorganizer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author Aleksander
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Long countAllByEmail(String email);

    User findByEmail(String email);

}
