package com.example.demo.domain.event;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;
import java.util.List;
import java.util.UUID;

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
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> retrieveById(@PathVariable UUID id) {
        return new ResponseEntity<>(eventService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable UUID id) {
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping({"", "/"})
    public ResponseEntity<EventDTO> updateEvent(@Valid @RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(eventService.updateEvent(eventDTO), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<EventDTO> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(eventService.addEvent(eventDTO), HttpStatus.CREATED);
    }

    @PutMapping("/guests/{id}")
    public ResponseEntity<EventDTO> addGuest(@PathVariable UUID id, @Valid @RequestBody MinimalUserDTO minimalUserDTO) {
        UserDTO userDTO = userMapper.toDTO(userMapper.fromMinimalUserDTO(minimalUserDTO));
        EventDTO eventDTO = eventService.findById(id);
        return new ResponseEntity<>(eventService.addGuest(eventDTO, userDTO), HttpStatus.OK);
    }

    @GetMapping("/guests/{id}")
    public ResponseEntity<Page<UserDTO>> retrieveAllGuests(@PathVariable UUID id, Pageable pageable) {
        return new ResponseEntity<>(eventService.findAllGuest(eventService.findById(id), pageable), HttpStatus.OK);
    }
}
