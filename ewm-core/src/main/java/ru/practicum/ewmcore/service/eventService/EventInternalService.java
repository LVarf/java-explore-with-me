package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;

import java.util.Optional;

public interface EventInternalService {
    @Transactional(readOnly = true)
    Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable);

    @Transactional
    Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event);

    @Transactional
    Optional<EventFullDto> createEvent(EventFullDto event);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEvent(Long eventId);

    @Transactional
    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    @Transactional
    Optional<EventFullDto> updateEvent(EventFullDto eventFullDto);
}
