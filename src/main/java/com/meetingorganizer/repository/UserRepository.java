package com.meetingorganizer.repository;

import com.meetingorganizer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by Aleksander on 21.10.2017.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Long countAllByEmail(String email);

}
