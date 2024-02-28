package com.example.demo.domain.event.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.user.dto.MinimalUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
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

    private List<MinimalUserDTO> guests;

    private MinimalUserDTO owner;

    public EventDTO(UUID id, String name, String date, String location, String description, List<MinimalUserDTO> guests, MinimalUserDTO owner) {
        super(id);
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.guests = guests;
        this.owner = owner;
    }

}
