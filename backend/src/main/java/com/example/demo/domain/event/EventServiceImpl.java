package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl extends AbstractServiceImpl<Event> implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    public EventServiceImpl(EventRepository repository){
        super(repository);
    }
@Override
    public Event addGuest(Event event, User guest) {
    log.info("Adding guest {} to event {}", guest.getId(), event.getId());
        List<User> guests = event.getGuests();


        // Check if the guest is already in the list
        if (guests.stream()
                .noneMatch(existingGuest -> existingGuest.getId().equals(guest.getId()))) {
            guests.add(guest);
            event.setGuests(guests);
            return save(event);
        }
    log.info("Guest {} is already added to event {}", guest.getId(), event.getId());
        return event;
    }

    @Override
    public Page<User> findAllGuest(UUID id, Pageable pageable) {
        log.info("Retrieving all guests of event with ID: {}", id);
        Optional<Event> event = repository.findById(id);
        List<User> guests = event.get().getGuests();

        int pageSize = Math.min(1, pageable.getPageSize());
        int pageBegin = (int) pageable.getOffset();
        int pageEnd = Math.min(pageBegin + pageSize, guests.size());
        List<User> subList = guests.subList(pageBegin, pageEnd);

        return new PageImpl<>(subList, pageable, guests.size());
    }
}
