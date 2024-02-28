package com.example.demo.domain.event;

import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;

@Service
public class EventServiceOld {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    public Page<User> findAllGuest(UUID id, Pageable pageable) {
        Optional<Event> event = eventRepository.findById(id);
        List<User> guests = event.get().getGuests();

        int pageSize = Math.min(1, pageable.getPageSize());
        int pageBegin = (int) pageable.getOffset();
        int pageEnd = Math.min(pageBegin + pageSize, guests.size());
        List<User> subList = guests.subList(pageBegin, pageEnd);

        return new PageImpl<>(subList, pageable, guests.size());
    }

    public Event addEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    public Event updateEvent(Event event) {
        eventRepository.save(event);
        return event;
    }

    public void deleteById(UUID id) {
        eventRepository.deleteById(id);
    }

    public Event addGuest(Event event, User guest) {
        List<User> guests = event.getGuests();


        // Check if the guest is already in the list
        if (guests.stream()
                .noneMatch(existingGuest -> existingGuest.getId().equals(guest.getId()))) {
            guests.add(guest);
            event.setGuests(guests);
            eventRepository.save(event);
        }
        return event;
    }

}
