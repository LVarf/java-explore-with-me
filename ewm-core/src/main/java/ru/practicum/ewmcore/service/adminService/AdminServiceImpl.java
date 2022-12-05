package ru.practicum.ewmcore.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminPublicService {
    private final EventInternalService eventService;

    @Override
    public Page<EventFullDto> readAllByFilters(ClientFilter filters, Pageable pageable) {
        eventService.readAllEventsByFilters(filters, pageable);
        return Page.empty();
    }

    @Override
    public Optional<EventFullDto> updateEventById(Long eventId, EventFullDto event) {
        return eventService.updateEventById(eventId, event);
    }

    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {

        return Optional.empty();
    }
}
