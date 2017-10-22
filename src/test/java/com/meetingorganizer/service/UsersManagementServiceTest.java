package com.meetingorganizer.service;

import com.meetingorganizer.domain.Authority;
import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.repository.UserRepository;
import com.meetingorganizer.repository.VerificationTokenRepository;
import com.meetingorganizer.service.impl.UsersManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Aleksander
 */
@RunWith(SpringRunner.class)
public class UsersManagementServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersManagementService userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isEmailAlreadyTaken_mailNotInRepository_ShouldReturnFalse(){
        String mail = "user@mail.com";

        when(userRepository.countAllByEmail(mail)).thenReturn(0L);
        boolean isMailTaken = userService.isEmailAlreadyTaken(mail);

        assertFalse(isMailTaken);
    }

    @Test
    public void isEmailAlreadyTaken_mailInRepository_ShouldReturnTrue() {
        String mail = "user@mail.com";

        when(userRepository.countAllByEmail(mail)).thenReturn(1L);
        boolean isMailTaken = userService.isEmailAlreadyTaken(mail);

        assertTrue(isMailTaken);
    }

    @Test
    public void getVerificationToken_tokenFound_ShouldReturnToken() {
        String token = "token";

        when(tokenRepository.findByToken(token)).thenReturn(new VerificationToken(token, new User()));
        VerificationToken tokenFromRepository = userService.getVerificationToken(token);

        assertNotNull(tokenFromRepository);
        assertEquals(token, tokenFromRepository.getToken());
    }

    @Test
    public void getVerificationToken_tokenNotFound_ShouldReturnNull() {
        when(tokenRepository.findByToken(anyString())).thenReturn(null);
        VerificationToken tokenFromRepository = userService.getVerificationToken("token");

        assertNull(tokenFromRepository);
    }

    @Test
    public void createVerificationToken_shouldCallTokenRepository() {
        String token = "token";
        User user = new User();

        userService.createVerificationToken(user, token);

        verify(tokenRepository, times(1)).saveAndFlush(any(VerificationToken.class));
    }


    @Test
    public void saveUser_shouldReturnSavedUser() {
        User user = new User();
        user.setEmail("user@mail.com");

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    public void generateNewVerificationToken_shouldCreateValidToken(){
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        VerificationToken oldToken = new VerificationToken("token", user);
        oldToken.setExpirationTime(LocalDateTime.now());
        VerificationToken newToken = new VerificationToken("tokenUpdated", user);
        newToken.setExpirationTime(oldToken.getExpirationTime().plusHours(24));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(tokenRepository.findByUser(user)).thenReturn(oldToken);
        when(tokenRepository.saveAndFlush(any(VerificationToken.class))).thenReturn(newToken);

        VerificationToken updatedToken = userService.generateNewVerificationToken("user@mail.com");

        assertNotNull(updatedToken);
        assertEquals(newToken.getExpirationTime(), updatedToken.getExpirationTime());
        assertEquals(newToken.getToken(), updatedToken.getToken());
        assertEquals(newToken.getUser(), updatedToken.getUser());
    }

    @Test
    public void registerUser_shouldCreateUserWithCorrectData() {
        RegistrationFormDto dto = new RegistrationFormDto();
        dto.setEmail("user@mail.pl");
        dto.setFirstName("first");
        dto.setLastName("last");
        dto.setPassword("password");

        Authority userAuthority = new Authority("USER");
        userAuthority.setId(1L);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(userAuthority);

        User userFromDto = new User(dto);
        userFromDto.setPassword("encodedPassword");
        userFromDto.setAuthorities(authorities);

        when(authorityService.findAuthorityByNameCreateAuthorityIfNotFound("USER"))
                .thenReturn(userAuthority);
        when(passwordEncoder.encode(userFromDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(userFromDto);

        User registeredUser = userService.registerUser(dto);

        assertNotNull(registeredUser);
        assertEquals(userFromDto.getEmail(), registeredUser.getEmail());
        assertEquals(userFromDto.getFirstName(), registeredUser.getFirstName());
        assertEquals(userFromDto.getLastName(), registeredUser.getLastName());
        assertEquals(userFromDto.getPassword(), registeredUser.getPassword());
        assertEquals(1, registeredUser.getAuthorities().size());
    }
}
