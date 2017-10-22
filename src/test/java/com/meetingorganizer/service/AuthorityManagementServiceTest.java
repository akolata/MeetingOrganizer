package com.meetingorganizer.service;

import com.meetingorganizer.domain.Authority;
import com.meetingorganizer.repository.AuthorityRepository;
import com.meetingorganizer.service.impl.AuthorityManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class AuthorityManagementServiceTest {

    @TestConfiguration
    static class AuthorityManagementServiceTestContextConfiguration{

        @Bean
        public AuthorityService authorityService() {
            return new AuthorityManagementService();
        }
    }

    @Autowired
    private AuthorityService authorityService;

    @MockBean
    private AuthorityRepository authorityRepository;

    @Test
    public void findAuthorityByNameCreateAuthorityIfNotFound_authorityFound_shouldReturnAuthorityFromDatabase() {
        String authorityUser = "USER";

        when(authorityRepository.findByAuthority(authorityUser)).thenReturn(new Authority(authorityUser));
        Authority foundAuthority = authorityService.findAuthorityByNameCreateAuthorityIfNotFound(authorityUser);

        assertNotNull(foundAuthority);
        assertEquals(authorityUser, foundAuthority.getAuthority());
    }

    @Test
    public void findAuthorityByNameCreateAuthorityIfNotFound_authorityNotFound_shouldCreateAuthority() {
        String authorityUser = "USER";
        Authority authority = new Authority(authorityUser);

        when(authorityRepository.findByAuthority(authorityUser)).thenReturn(null);
        when(authorityRepository.saveAndFlush(any(Authority.class))).thenReturn(authority);

        Authority foundAuthority = authorityService.findAuthorityByNameCreateAuthorityIfNotFound(authorityUser);

        assertNotNull(foundAuthority);
        assertEquals("USER", foundAuthority.getAuthority());
    }

}
