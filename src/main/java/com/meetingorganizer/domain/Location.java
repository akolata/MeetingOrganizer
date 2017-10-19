package com.meetingorganizer.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Location table - places were meeting can be organized
 *
 * @author Aleksander
 */
@Entity
@Table(name = "LOCATION")
public class Location {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Column(name = "NAME", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "DESCRIPTION")
    @Getter
    @Setter
    private String description;

    @Column(name = "MAX_MEMBERS", nullable = false)
    @Getter
    @Setter
    private Integer maxMembers;

    @OneToMany
    @JoinColumn(name = "LOCATION_ID")
    @Getter
    @Setter
    private List<Reservation> reservations;

    public Location() {
        this.reservations = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxMembers=" + maxMembers +
                '}';
    }
}
