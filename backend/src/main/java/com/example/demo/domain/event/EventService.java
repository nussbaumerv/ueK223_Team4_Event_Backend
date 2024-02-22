package com.example.demo.domain.event;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    //TODO write CRUD Methods

    public List<EventDTO> findAll() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDTOs(events);
    }

    public EventDTO findById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
        return eventMapper.toDTO(event);
    }

    public EventDTO addEvent(EventDTO eventDTO) {
        Event event = eventMapper.fromDTO(eventDTO);
        eventRepository.save(event);
        return eventMapper.toDTO(event);
    }

    public EventDTO updateEvent(EventDTO eventDTO) {
        Event event = eventMapper.fromDTO(eventDTO);
        eventRepository.save(event);
        return eventMapper.toDTO(event);
    }

    public void deleteById(UUID id) {
        eventRepository.deleteById(id);
    }

    public EventDTO addGuest(EventDTO eventDTO, UserDTO guestDTO) {
        Event event = eventMapper.fromDTO(eventDTO);
        User guest = userMapper.fromDTO(guestDTO);
        List<User> guests = event.getGuests();
        guests.add(guest);
        event.setGuests(guests);
        eventRepository.save(event);
        return eventMapper.toDTO(event);
    }

}
