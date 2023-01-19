package ru.practicum.ewmcore.service.eventToCompilationService;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;

import java.util.Set;

@Transactional
public interface EventToCompilationInternalService {
    EventToCompilation createEventToCompilation(Event event, Compilation compilation);

    String deleteEventFromCompilation(Long eventId, Long compId);

    @Transactional(readOnly = true)
    Set<EventToCompilation> readEventToCompilation(Long comId);
}
