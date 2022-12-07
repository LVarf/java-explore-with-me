package ru.practicum.ewmcore.service.eventService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

public interface EventPublicService {

    Page<EventShortDto> readAllEventsPublic(ClientFilter filter, String sort, Pageable pageable);
}
