package com.example.demo.domain.event;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;
import java.util.List;
import java.util.UUID;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import com.example.demo.domain.user.dto.MinimalUserDTO;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    private final UserService userService;

    private final UserMapper userMapper;


    @Autowired
    public EventController(EventService eventService, EventMapper eventMapper, UserService userService, UserMapper userMapper) {
        this.eventService = eventService;
        this.eventMapper =  eventMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<EventDTO>> retrieveAll() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(eventMapper.toDTOs(events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> retrieveById(@PathVariable UUID id) {
        Event event = eventService.findById(id);
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable UUID id) {
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping({"", "/"})
    public ResponseEntity<EventDTO> updateEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = eventService.updateEvent(eventMapper.fromDTO(eventDTO));
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<EventDTO> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = eventService.addEvent(eventMapper.fromDTO(eventDTO));
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/guests/")
    public ResponseEntity<EventDTO> addGuest(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        Event event = eventService.addGuest(eventService.findById(id), userMapper.fromDTO(userDTO));
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
    }

    @GetMapping("/{id}/guests/")
    public ResponseEntity<Page<UserDTO>> retrieveAllGuests(@PathVariable UUID id, Pageable pageable) {
        Page<UserDTO> users = eventService.findAllGuest(id, pageable).map(userMapper::toDTO);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
