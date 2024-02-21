package com.example.demo.domain.event;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;
import com.example.demo.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    //TODO write CRUD Methods

    public List<EventDTO> findAll() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDTOs(events);
    }

}
