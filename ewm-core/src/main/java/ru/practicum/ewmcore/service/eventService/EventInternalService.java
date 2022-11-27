package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.Event;

public interface EventInternalService {
    @Transactional(readOnly = true)
    Page<Event> readAllByInitiatorId(Long id, Pageable pageable);
}
