package com.example.demo.domain.event.dto;

import com.example.demo.core.generic.AbstractMapper;
import com.example.demo.domain.event.Event;
import com.example.demo.domain.user.dto.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface EventMapper extends AbstractMapper<Event, EventDTO> {
}

