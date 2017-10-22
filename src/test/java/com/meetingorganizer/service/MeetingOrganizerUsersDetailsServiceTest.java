package com.meetingorganizer.service;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.repository.UserRepository;
import com.meetingorganizer.service.impl.MeetingOrganizerUsersDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class MeetingOrganizerUsersDetailsServiceTest {

    @TestConfiguration
    static class UsersDetailsServiceTestContextConfiguration {

        @Bean
        public UserDetailsService userDetailsService() {
            return new MeetingOrganizerUsersDetailsService();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsername_userMaiIsInRepository_ShouldReturnUser(){
        String mail = "user@mail.com";
        User userFromRepository = new User();
        userFromRepository.setEmail(mail);

        when(userRepository.findByEmail(mail)).thenReturn(userFromRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(mail);

        assertNotNull(userDetails);
        assertEquals(mail, userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_userMailNotInRepository_ShouldThrowException() {
        String mail = "user@mail.com";

        when(userRepository.findByEmail(mail)).thenReturn(null);
        userDetailsService.loadUserByUsername(mail);
    }
}
