package com.example.demo.domain.event;

import com.example.demo.core.generic.AbstractRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends AbstractRepository<Event> {

}
