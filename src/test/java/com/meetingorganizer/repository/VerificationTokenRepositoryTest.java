package com.meetingorganizer.repository;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static junit.framework.TestCase.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VerificationTokenRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    private User user;

    @Before
    public void setup(){
        user = new User();
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("password");
        user.setEmail("user@mail.com");

        testEntityManager.persistAndFlush(user);
    }

    @Test
    public void findByToken_recordFound_shouldReturnValidToken() {
        String token = UUID.randomUUID().toString();
        VerificationToken persistedToken = new VerificationToken(token, user);

        testEntityManager.persistAndFlush(persistedToken);
        VerificationToken foundToken = tokenRepository.findByToken(token);

        assertNotNull(foundToken);
        assertEquals(persistedToken.getToken(), foundToken.getToken());
        assertEquals(persistedToken.getId(), foundToken.getId());
        assertEquals(persistedToken.getUser(), foundToken.getUser());
        assertEquals(persistedToken.getExpirationTime(), foundToken.getExpirationTime());
    }

    @Test
    public void findByToken_recordNotFound_shouldReturnNull() {
        String token = UUID.randomUUID().toString();
        String otherToken = UUID.randomUUID().toString();
        VerificationToken persistedToken = new VerificationToken(token, user);

        testEntityManager.persistAndFlush(persistedToken);
        VerificationToken foundToken = tokenRepository.findByToken(otherToken);

        assertNull(foundToken);
    }

    @Test
    public void findByUser_recordFound_shouldReturnValidToken() {
        String token = UUID.randomUUID().toString();
        VerificationToken persistedToken = new VerificationToken(token, user);

        testEntityManager.persistAndFlush(persistedToken);
        VerificationToken foundToken = tokenRepository.findByUser(user);

        assertNotNull(foundToken);
        assertEquals(persistedToken.getToken(), foundToken.getToken());
        assertEquals(persistedToken.getId(), foundToken.getId());
        assertEquals(persistedToken.getUser(), foundToken.getUser());
        assertEquals(persistedToken.getExpirationTime(), foundToken.getExpirationTime());
    }

    @Test
    public void findByUser_recordNotFound_shouldReturnValidToken() {
        String token = UUID.randomUUID().toString();
        User otherUser = new User();
        otherUser.setFirstName("first");
        otherUser.setLastName("last");
        otherUser.setPassword("password");
        otherUser.setEmail("other.user@mail.com");

        testEntityManager.persistAndFlush(otherUser);
        VerificationToken persistedToken = new VerificationToken(token, user);

        testEntityManager.persistAndFlush(persistedToken);
        VerificationToken foundToken = tokenRepository.findByUser(otherUser);

        assertNull(foundToken);
    }
}
