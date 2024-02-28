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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@Validated
@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    private final UserMapper userMapper;


    @Autowired
    public EventController(EventService eventService, EventMapper eventMapper, UserMapper userMapper) {
        this.eventService = eventService;
        this.eventMapper =  eventMapper;
        this.userMapper = userMapper;
    }

    public UUID getRequestingUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        return currentUser.user().getId();
    }

    @Operation(summary = "Retrieve all events", description = "Returns all events.")
    @GetMapping({"", "/"})
    public ResponseEntity<List<EventDTO>> retrieveAll() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(eventMapper.toDTOs(events));
    }

    @Operation(summary = "Retrieve event by ID", description = "Returns a specific event by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> retrieveById(@PathVariable UUID id) {
        Event event = eventService.findById(id);
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.OK);
    }

    @Operation(summary = "Delete event by ID", description = "Deletes a specific event by its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable UUID id) {
        Event event = eventService.findById(id);
        if(event.getOwner().getId().equals(getRequestingUserId())) {
            eventService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Update event", description = "Updates an existing event.")
    @PutMapping({ "/id"})
    @PreAuthorize(" @eventPermissionEvaluator.isOwner(eventDTO, authentication.getPrincipal().user)")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable UUID id, @Valid @RequestBody EventDTO eventDTO) {
        Event event = eventMapper.fromDTO(eventDTO);
            Event updatedEvent = eventService.updateById(id, event);
            return new ResponseEntity<>(eventMapper.toDTO(updatedEvent), HttpStatus.OK);
    }

    @Operation(summary = "Add new event", description = "Adds a new event.")
    @PostMapping({"", "/"})
    public ResponseEntity<EventDTO> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = eventService.save(eventMapper.fromDTO(eventDTO));
        return new ResponseEntity<>(eventMapper.toDTO(event), HttpStatus.CREATED);
    }

    @Operation(summary = "Add guest to event", description = "Adds a guest to a specific event.")
    @PutMapping("/{id}/guests/")
    public ResponseEntity<EventDTO> addGuest(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        Event event = eventService.findById(id);
        User newGuest = userMapper.fromDTO(userDTO);
        if(event.getOwner().getId().equals(getRequestingUserId()) && !newGuest.getRoles().contains("Admin") && !event.getOwner().getId().equals(newGuest.getId())) {
            Event eventUpdated = eventService.addGuest(eventService.findById(id), newGuest);
            return new ResponseEntity<>(eventMapper.toDTO(eventUpdated), HttpStatus.OK);
        } else{
            System.out.println(newGuest.getRoles() + " / " + getRequestingUserId() + " / " + event.getOwner().getEmail());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @Operation(summary = "Retrieve all guests of an event", description = "Returns all guests of a specific event.")
    @GetMapping("/{id}/guests/")
    public ResponseEntity<Page<UserDTO>> retrieveAllGuests(@PathVariable UUID id, Pageable pageable) {
        Page<UserDTO> users = eventService.findAllGuest(id, pageable).map(userMapper::toDTO);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
