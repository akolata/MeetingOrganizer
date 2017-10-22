package com.meetingorganizer.repository;

import com.meetingorganizer.domain.Authority;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AuthorityRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void findByAuthority_noRecordsFound_shouldReturnNull() {
        Authority authority = new Authority("USER");
        String authorityNameToSearchFor = "ADMIN";

        testEntityManager.persistAndFlush(authority);
        Authority foundAuthority = authorityRepository.findByAuthority(authorityNameToSearchFor);

        assertNull(foundAuthority);
    }

    @Test
    public void findByAuthority_recordsFound_shouldReturnValidAuthority() {
        Authority authority = new Authority("USER");

        testEntityManager.persistAndFlush(authority);
        Authority foundAuthority = authorityRepository.findByAuthority("USER");

        assertNotNull(foundAuthority);
        assertEquals(authority.getId(), foundAuthority.getId());
        assertEquals(authority.getAuthority(), foundAuthority.getAuthority());
    }
}
