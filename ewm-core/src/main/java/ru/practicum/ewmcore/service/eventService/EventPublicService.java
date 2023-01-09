package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.List;
import java.util.Optional;

public interface EventPublicService {

    List<EventShortDto> readAllEventsPublic(ClientFilter filter, String sort, Pageable pageable);

    Optional<EventFullDto> readEventPublic(Long eventId);
}
