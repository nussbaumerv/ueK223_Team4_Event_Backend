package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserDetailsImpl;
import com.example.demo.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends AbstractServiceImpl<Event> implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public EventServiceImpl(EventRepository repository,
                            UserRepository userRepository) {
        super(repository);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Event addGuest(Event event, User guest) {
        log.debug("Adding guest {} to event {}", guest.getId(), event.getId());
        List<User> guests = event.getGuests();

        if (guests.stream().noneMatch(existingGuest -> existingGuest.getId().equals(guest.getId()))) {
            guests.add(guest);
            event.setGuests(guests);
            save(event);
        } else {
            log.info("Guest {} is already added to event {}", guest.getId(), event.getId());
        }
        return event;
    }

    @Override
    public Page<User> findAllGuest(UUID id, Pageable pageable) {
        log.debug("Retrieving all guests of event with ID: {}", id);

        Event event = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id: " + id + " not found"));

        List<User> guests = event.getGuests();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), guests.size());

        return new PageImpl<>(guests.subList(start, end), pageable, guests.size());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void removeUserFromAllEvents(User guest) {
        log.debug("Removing guest from all events with ID: {}", guest.getId());
        //Remove user from all events where in guests
      /*  ((EventRepository) repository).findByGuests_Id(guest.getId()).stream().forEach(user -> {
            List<User> guests = event.getGuests();
            guests.remove(guest);
            event.setGuests(guests);
            save(event);
        }); */
        findAll().stream()
                .filter(event -> event.getGuests().contains(guest))
                .forEach(event -> {
                    List<User> guests = event.getGuests();
                    guests.remove(guest);
                    event.setGuests(guests);
                    save(event);
                });

        //Remove user from all events where owner
        findAll().stream().filter(event -> event.getOwner().equals(guest))
                .forEach(event -> {
                    event.setOwner(null);
                });
    }

    @Override
    public List<User> findAllPossibleGuests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl requestingUserImpl = (UserDetailsImpl) authentication.getPrincipal();
        User requestingUser = requestingUserImpl.user();

        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("PARTICIPATE_EVENT"))))
                .filter(user -> !user.getId().equals(requestingUser.getId()))
                .collect(Collectors.toList());
    }
}
