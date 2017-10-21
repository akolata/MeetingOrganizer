package com.meetingorganizer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Table with verification tokens used for login registration
 * @author Aleksander
 */
@Entity
@Table(name = "VERIFICATION_TOKEN")
@Getter @Setter
public class VerificationToken {

    private static final int EXPIRATION_TIME_IN_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    @Column(name = "EXPIRATION_TIME")
    private LocalDateTime expirationTime;

    public VerificationToken(){}

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME_IN_HOURS);
    }

    private LocalDateTime calculateExpirationDate(int expirationTimeInMinutes){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime =  now.plusHours(expirationTimeInMinutes);
        return expirationTime;
    }

    public boolean isTokenExpired(){
        return LocalDateTime.now().isAfter(this.expirationTime);
    }

}
