package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.demo.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class   Event extends AbstractEntity {
    @NotNull
    @Column
    private String name;
    @NotNull
    @Column
    private String date;
    @NotNull
    @Column
    private String location;
    @NotNull
    @Column
    private String description;

    @ManyToMany
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
