package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.event.EventService;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventPermissionEvaluator {
    private final UserMapper userMapper;

    private final UserService userService;

    private final EventService eventService;

    public EventPermissionEvaluator(UserMapper userMapper, EventService eventService, UserService userService) {
        this.userMapper = userMapper;
        this.eventService = eventService;
        this.userService = userService;
    }

    public boolean isOwner(UUID eventId, User userFromRequest) {
        //Gets the user from the Service to ensure, that user data is up to date
        User requestingUser = userService.findById(userFromRequest.getId());
        User owner = eventService.findById(eventId).getOwner();
        return owner.getId().equals(requestingUser.getId());
    }

    public boolean isGuestValid(UUID eventId, UserDTO guestDTO) {
        User guestFromRequest = userMapper.fromDTO(guestDTO);

        //Gets the guest from the Service to ensure, that the roles weren't modified
        User guest = userService.findById(guestFromRequest.getId());

        boolean hasAuthority = guest.getRoles().stream()
                .anyMatch(role -> role.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("PARTICIPATE_EVENT")));
        return hasAuthority && !isOwner(eventId, guest);
    }
}
