package com.example.demo.domain.event.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.role.dto.RoleDTO;
import com.example.demo.domain.user.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class EventDTO extends AbstractDTO {

    private String name;

    private String date;

    private String location;

    private String description;

    private List<User> guests;

    private User owner;
        public EventDTO(UUID id, String name, String date, String location, String description, List<User> guests, User owner) {
            super(id);
            this.name = name;
            this.date = date;
            this.location = location;
            this.description = description;
            this.guests = guests;
            this.owner = owner;
        }

    }
