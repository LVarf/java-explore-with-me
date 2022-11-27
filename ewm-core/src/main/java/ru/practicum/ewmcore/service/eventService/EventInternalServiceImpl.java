package ru.practicum.ewmcore.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventInternalServiceImpl implements EventInternalService {
    private final EventRepository eventRepository;

    @Override
    public Page<Event> readAllByInitiatorId(Long id, Pageable pageable) {
        return eventRepository.findAllByInitiator(id, pageable);
    }
}
