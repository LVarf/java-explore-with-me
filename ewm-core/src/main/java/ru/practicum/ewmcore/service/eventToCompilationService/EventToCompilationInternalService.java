package ru.practicum.ewmcore.service.eventToCompilationService;

import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;

import java.util.List;
import java.util.Set;

public interface EventToCompilationInternalService {
    EventToCompilation createEventToCompilation(Event event, Compilation compilation);

    String deleteEventFromCompilation(Long eventId, Long compId);

    Set<EventToCompilation> readEventToCompilation(Long comId);
}
