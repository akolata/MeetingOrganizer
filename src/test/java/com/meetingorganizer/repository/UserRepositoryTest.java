package com.meetingorganizer.repository;

import com.meetingorganizer.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void countAllByEmail_noRecordsFound_shouldReturn0() {
        String userMail = "user@mail.com",
                searchedMail = "other@mail.com";
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("password");
        user.setEmail(userMail);

        testEntityManager.persistAndFlush(user);

        Long usersWithSameEmail = userRepository.countAllByEmailIgnoreCase(searchedMail);

        assertEquals(new Long(0), usersWithSameEmail);
    }

    @Test
    public void countAllByEmail_recordsFound_shouldReturnValidNumberOfRecords() {
        String userMail = "user@mail.com";
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("password");
        user.setEmail(userMail);

        testEntityManager.persistAndFlush(user);


        Long usersWithSameEmail = userRepository.countAllByEmailIgnoreCase(userMail);
        assertEquals(new Long(1), usersWithSameEmail);
    }

    @Test
    public void findByEmail_noRecordFound_shouldReturnNull (){
        String userMail = "user@mail.com",
                searchedMail = "other@mail.com";
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("password");
        user.setEmail(userMail);

        testEntityManager.persistAndFlush(user);

        User userFound = userRepository.findByEmail(searchedMail);

        assertNull(userFound);
    }

    @Test
    public void findByEmail_recordFound_shouldReturnUser (){
        String userMail = "user@mail.com";
        User user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("password");
        user.setEmail(userMail);

        User persistedUser = testEntityManager.persistAndFlush(user);
        User foundUser = userRepository.findByEmail(userMail);

        assertNotNull(foundUser);
        assertEquals(persistedUser.getId(), foundUser.getId());
    }
}
