package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends AbstractRepository<Event> {
    List<Event> findByGuests_Id(UUID id);

    List<Event> findByOwner_Id(UUID id);
}
