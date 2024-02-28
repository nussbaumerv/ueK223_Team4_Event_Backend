package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.event.EventRepository;
import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl extends AbstractServiceImpl<Event> implements EventService {
    @Autowired
    public EventServiceImpl(EventRepository repository){
        super(repository);
    }
@Override
    public Event addGuest(Event event, User guest) {
        List<User> guests = event.getGuests();


        // Check if the guest is already in the list
        if (guests.stream()
                .noneMatch(existingGuest -> existingGuest.getId().equals(guest.getId()))) {
            guests.add(guest);
            event.setGuests(guests);
            return save(event);
        }
        return event;
    }

    @Override
    public Page<User> findAllGuest(UUID id, Pageable pageable) {
        Optional<Event> event = repository.findById(id);
        List<User> guests = event.get().getGuests();

        int pageSize = Math.min(1, pageable.getPageSize());
        int pageBegin = (int) pageable.getOffset();
        int pageEnd = Math.min(pageBegin + pageSize, guests.size());
        List<User> subList = guests.subList(pageBegin, pageEnd);

        return new PageImpl<>(subList, pageable, guests.size());
    }
}
