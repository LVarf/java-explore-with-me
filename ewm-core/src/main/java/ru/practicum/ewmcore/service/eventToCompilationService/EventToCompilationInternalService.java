package ru.practicum.ewmcore.service.eventToCompilationService;

import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;

public interface EventToCompilationInternalService {
    EventToCompilation createEventToCompilation(Event event, Compilation compilation);

    String deleteEventFromCompilation(Long eventId, Long compId);
}
