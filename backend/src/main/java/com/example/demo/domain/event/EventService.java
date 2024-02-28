package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractService;
import com.example.demo.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService extends AbstractService<Event> {
    Event addGuest(Event event, User guest);
    Page<User> findAllGuest(UUID id, Pageable pageable);
    void removeGuestFromAllEvents(User guest);
}
