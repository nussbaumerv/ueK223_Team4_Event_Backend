package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractEntity;

import java.util.List;
import java.util.UUID;

import com.example.demo.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "event")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Event extends AbstractEntity {
    @NotNull
    @Column
    @Size(min = 1, max = 100)
    private String name;
    @NotNull
    @Column
    @Size(min = 6, max = 26)
    private String date;
    @NotNull
    @Column
    @Size(min = 1, max = 100)
    private String location;
    @NotNull
    @Column
    @Size(min = 1, max = 900)
    private String description;

    @ManyToMany
    @JoinTable(name = "event_guests",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "guests_id", referencedColumnName = "id"))
    private List<User> guests;

    @ManyToOne
    private User owner;

    public Event(UUID id, String name, String date, String location, String description, List<User> guests, User owner) {
        super(id);
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.guests = guests;
        this.owner = owner;
    }
}
