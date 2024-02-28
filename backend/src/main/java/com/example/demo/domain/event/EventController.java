package com.example.demo.domain.event;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;

import java.util.List;
import java.util.UUID;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserDetailsImpl;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Validated
@RestController
@RequestMapping("/event")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Autowired
    public EventController(EventService eventService, EventMapper eventMapper, UserMapper userMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    public UUID getRequestingUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        return currentUser.user().getId();
    }

    @Operation(summary = "Retrieve all events", description = "Returns all events.")
    @GetMapping({"", "/"})
    public ResponseEntity<List<EventDTO>> retrieveAll() {
        log.info("Retrieving all events.");
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(eventMapper.toDTOs(events));
    }

    @Operation(summary = "Retrieve event by ID", description = "Returns a specific event by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> retrieveById(@PathVariable UUID id) {
        log.info("Retrieving event by ID: {}", id);
        Event event = eventService.findById(id);
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
    }

    @Operation(summary = "Delete event by ID", description = "Deletes a specific event by its unique ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize(" @eventPermissionEvaluator.isOwner(#id, authentication.getPrincipal().user)")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable UUID id) {
        log.info("Deleting event by ID: {}", id);
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update event", description = "Updates an existing event.")
    @PutMapping("/{id}")
    @PreAuthorize(" @eventPermissionEvaluator.isOwner(#id, authentication.getPrincipal().user)")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable UUID id, @Valid @RequestBody EventDTO eventDTO) {
        log.info("Updating event with ID: {}", id);
        Event event = eventMapper.fromDTO(eventDTO);
        Event updatedEvent = eventService.updateById(id, event);
        return new ResponseEntity<>(eventMapper.toDTO(updatedEvent), HttpStatus.OK);
    }

    @Operation(summary = "Add new event", description = "Adds a new event.")
    @PostMapping({"", "/"})
    public ResponseEntity<EventDTO> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        log.info("Adding a new event.");
        Event event = eventService.save(eventMapper.fromDTO(eventDTO));
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.CREATED);
    }

    @Operation(summary = "Add guest to event", description = "Adds a guest to a specific event.")
    @PutMapping("/{id}/guests/")
    @PreAuthorize(" @eventPermissionEvaluator.isOwner(#id, authentication.getPrincipal().user) && @eventPermissionEvaluator.isGuestValid(#id, #userDTO)")
    public ResponseEntity<EventDTO> addGuest(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        log.info("Adding guest to event with ID: {}", id);
        User newGuest = userMapper.fromDTO(userDTO);
        Event eventUpdated = eventService.addGuest(eventService.findById(id), newGuest);
        return new ResponseEntity<>(eventMapper.toDTO(eventUpdated), HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all guests of an event", description = "Returns all guests of a specific event.")
    @GetMapping("/{id}/guests/")
    public ResponseEntity<Page<UserDTO>> retrieveAllGuests(@PathVariable UUID id, Pageable pageable) {
        log.info("Retrieving all guests of event with ID: {}", id);
        Page<UserDTO> users = eventService.findAllGuest(id, pageable).map(userMapper::toDTO);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
