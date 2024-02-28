package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.event.EventRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventPermissionEvaluator {
    private final UserMapper userMapper;
    private final EventRepository eventRepository;

    public EventPermissionEvaluator(UserMapper userMapper, EventRepository eventRepository) {
        this.userMapper = userMapper;
        this.eventRepository = eventRepository;
    }

    public boolean isOwner(UUID eventId, User requestingUser) {
        User owner = eventRepository.findById(eventId).get().getOwner();
        return owner.getId().equals(requestingUser.getId());
    }

    public boolean isGuestValid(UUID eventId, UserDTO guestDTO) {
        User guest = userMapper.fromDTO(guestDTO);
        return !guest.getRoles().contains("Admin") && !isOwner(eventId, guest);
    }
}
