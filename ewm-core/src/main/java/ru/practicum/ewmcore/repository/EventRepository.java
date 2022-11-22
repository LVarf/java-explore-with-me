package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmcore.model.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
