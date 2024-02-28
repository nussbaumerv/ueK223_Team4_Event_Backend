package com.example.demo.core.security.permissionevaluators;

import com.example.demo.domain.event.dto.EventDTO;
import com.example.demo.domain.event.dto.EventMapper;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class EventPermissionEvaluator {
    private final EventMapper eventMapper;
    public EventPermissionEvaluator(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }
        public boolean isOwner(EventDTO event, User requestingUser) {
            User owner = eventMapper.fromDTO(event).getOwner();
            return owner.getId().equals(requestingUser.getId());
        }
}
