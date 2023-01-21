package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventInternalService {
    Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable);

    Optional<Event> readById(Long eventId);

    Page<EventFullDto> readAllEventsByFilters(ClientFilter filters, Pageable pageable);

    Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event);

    Optional<EventFullDto> createEvent(EventFullDto event);

    Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId);

    Optional<EventFullDto> readEvent(Long eventId);

    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    Optional<Event> updateEvent(Event event);

    Optional<EventFullDto> updateEventById(Long eventId, EventFullDto eventFullDto);

    Optional<EventFullDto> updateEventOnPublish(Long eventId);

    Optional<EventFullDto> updateEventToReject(Long eventId);

    List<EventFullDto> readAllByCategoryId(Long catId);

    List<Event> readAllByIds(Set<Long> eventIds);
}
