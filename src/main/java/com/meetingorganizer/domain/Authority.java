package com.meetingorganizer.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User's authorities
 *
 * @author Aleksander
 */
@Entity
@Table(name = "AUTHORITY")
public class Authority
        implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, unique = true)
    @Getter
    @Setter
    private Long id;

    @Column(name = "AUTHORITY", nullable = false, unique = true)
    @Setter // Getter is overrided
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    @Getter
    @Setter
    private Set<User> users;

    public Authority() {
        this.users = new HashSet<>();
    }

    public Authority(String authority) {
        this.authority = authority;
        this.users = new HashSet<>();
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        return id.equals(authority.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
