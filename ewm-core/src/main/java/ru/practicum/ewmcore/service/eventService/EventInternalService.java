package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface EventInternalService {
    @Transactional(readOnly = true)
    Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<Event> readById(Long eventId);

    @Transactional(readOnly = true)
    Page<EventFullDto> readAllEventsByFilters(ClientFilter filters, Pageable pageable);

    Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event);

    Optional<EventFullDto> createEvent(EventFullDto event);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEvent(Long eventId);

    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    Optional<Event> updateEvent(Event event);

    Optional<EventFullDto> updateEventById(Long eventId, EventFullDto eventFullDto);

    Optional<EventFullDto> updateEventOnPublish(Long eventId);

    Optional<EventFullDto> updateEventToReject(Long eventId);

    @Transactional(readOnly = true)
    List<EventFullDto> readAllByCategoryId(Long catId);

    @Transactional(readOnly = true)
    List<Event> readAllByIds(Set<Long> eventIds);
}
